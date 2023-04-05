package com.moutamid.annoe.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.moutamid.annoe.R;
import com.moutamid.annoe.constants.Constants;
import com.moutamid.annoe.databinding.FragmentHomeBinding;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private WebSocketClient webSocketClient;
    private WebSocketClient webTrainClient;
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
        });

        createWebSocketClient();
        //createTrainClient();

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
                                binding.battery.setText("24%");
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

    @Override
    public void onPause() {
        super.onPause();
        webSocketClient.close();
        webTrainClient.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocketClient.close();
        webTrainClient.close();
    }
}