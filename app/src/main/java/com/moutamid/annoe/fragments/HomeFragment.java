package com.moutamid.annoe.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.moutamid.annoe.R;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.FragmentHomeBinding;

import org.json.JSONObject;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import tech.gusavila92.websocketclient.WebSocketClient;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private WebSocketClient webSocketClient;
    private WebSocketClient webTrainClient;
    private boolean isRunning = false;
    Thread thread;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        binding.retrain.setOnClickListener(v -> {
            webSocketClient.close();
            binding.cpu.setText("0%");
            binding.battery.setText("0%");
            binding.memory.setText("0MB");
            binding.result.setText("");
            createWebSocketClient();
            createTrainClient();

            binding.switchStress.setClickable(false);
            binding.retrain.setEnabled(false);
            binding.retrain.setCardBackgroundColor(requireContext().getResources().getColor(R.color.purpleD));
        });

        createWebSocketClient();
        //createTrainClient();

        binding.switchStress.setOnClickListener(v -> {
            if (isRunning){
                isRunning = false;
                binding.switchStress.setImageResource(R.drawable.switch_off);
            } else {
                isRunning = true;
                binding.switchStress.setImageResource(R.drawable.switch_on);
                startStressTest();
            }
        });

        binding.setting.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.frame, new SettingFragment()).commit();
        });

        return binding.getRoot();
    }

    private void createTrainClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI(Constants.TRAIN_DATA);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webTrainClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webTrainClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.i("WebSocket", message);
                            binding.result.setText(message + "\n\n" + binding.result.getText().toString());
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.result.setText("Connection was closed successfully." + "\n\n" + binding.result.getText().toString());
                        binding.retrain.setEnabled(true);
                        binding.switchStress.setClickable(true);
                        binding.retrain.setCardBackgroundColor(requireContext().getResources().getColor(R.color.purple));
                    }
                });
            }
        };

        webTrainClient.setConnectTimeout(10000);
        webTrainClient.setReadTimeout(60000);
        webTrainClient.enableAutomaticReconnection(5000);
        webTrainClient.connect();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI(Constants.LIVE_DATA);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.i("WebSocket", message);
                            JSONObject object = new JSONObject(message);
                            double cpu = object.getDouble("cpu_usage");
                            binding.cpu.setText(cpu + "%");
                            JSONObject memory = object.getJSONObject("memory_usage");
                            long mem = memory.getLong("used");
                            String size;
                            double size_kb = mem / 1024;
                            double size_mb = size_kb / 1024;
                            double size_gb = size_mb / 1024 ;

                            if (size_gb > 1){
                                size = String.format("%.2f", size_gb) + " GB";
                            }else if(size_mb > 0){
                                size = String.format("%.2f", size_mb) + " MB";
                            }else{
                                size = String.format("%.2f", size_kb) + " KB";
                            }

                            binding.memory.setText(size);

                            JSONObject battery = object.getJSONObject("battery");
                            String perc = battery.getString("percent");
                            if (perc.equals("N/A")){
                                binding.battery.setText("N/A");
                            } else {
                                binding.battery.setText(perc + "%");
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private void startStressTest() {
        new Thread(() -> {

            while (isRunning) {
                int itr = Stash.getInt(Constants.ITR, 100);
                int mat = Stash.getInt(Constants.MAT, 100);
                new Stress().stress(itr, mat);
            }

            /*
            // Stress the CPU by calculating a large prime number
            BigInteger prime = BigInteger.valueOf(Long.MAX_VALUE);
            while (isRunning) {
                prime = prime.nextProbablePrime();
            }
            // Allocate a large amount of memory
            int[] arr = new int[1000000];
            for (int i = 0; i < 1000000 && isRunning; i++) {
                arr[i] = i;
            }

            */
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        webSocketClient.close();
//        webTrainClient.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocketClient.close();
        try{
            webTrainClient.close();
        } catch (Exception e){

        }
    }

    public class Stress {

        public Stress() {
        }

        public void stress(int iterations, int matrixSize) {
            Random random = new Random();
            double[][] matrixA = generateRandomMatrix(matrixSize, random);
            double[][] matrixB = generateRandomMatrix(matrixSize, random);

            for (int i = 0; i < iterations; i++) {
                multiplyMatrices(matrixA, matrixB);
                allocateMemory(matrixSize);
            }
        }

        private double[][] generateRandomMatrix(int matrixSize, Random random) {
            double[][] matrix = new double[matrixSize][matrixSize];
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    matrix[i][j] = random.nextDouble();
                }
            }
            return matrix;
        }

        private double[][] multiplyMatrices(double[][] a, double[][] b) {
            int rowsA = a.length;
            int colsA = a[0].length;
            int colsB = b[0].length;
            double[][] result = new double[rowsA][colsB];

            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    double sum = 0;
                    for (int k = 0; k < colsA; k++) {
                        sum += a[i][k] * b[k][j];
                    }
                    result[i][j] = sum;
                }
            }
            return result;
        }

        private void allocateMemory(int size) {
            int[] array = new int[size * 10000];
            for (int i = 0; i < array.length; i++) {
                array[i] = i;
            }
        }
    }


}