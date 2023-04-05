package com.moutamid.annoe.models;

public class Model {
    String id, model, version;
    int elapsed_time;
    double accuracy, loss;
    String created_at;
    int epochs;
    String prediction_ratio;
    int model_size;
    String aware_optimizer, post_optimizer;

    public Model() {
    }

    public Model(String id, String model, String version, int elapsed_time, double accuracy, double loss, String created_at, int epochs, String prediction_ratio, int model_size, String aware_optimizer, String post_optimizer) {
        this.id = id;
        this.model = model;
        this.version = version;
        this.elapsed_time = elapsed_time;
        this.accuracy = accuracy;
        this.loss = loss;
        this.created_at = created_at;
        this.epochs = epochs;
        this.prediction_ratio = prediction_ratio;
        this.model_size = model_size;
        this.aware_optimizer = aware_optimizer;
        this.post_optimizer = post_optimizer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(int elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
        this.loss = loss;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getEpochs() {
        return epochs;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }

    public String getPrediction_ratio() {
        return prediction_ratio;
    }

    public void setPrediction_ratio(String prediction_ratio) {
        this.prediction_ratio = prediction_ratio;
    }

    public int getModel_size() {
        return model_size;
    }

    public void setModel_size(int model_size) {
        this.model_size = model_size;
    }

    public String getAware_optimizer() {
        return aware_optimizer;
    }

    public void setAware_optimizer(String aware_optimizer) {
        this.aware_optimizer = aware_optimizer;
    }

    public String getPost_optimizer() {
        return post_optimizer;
    }

    public void setPost_optimizer(String post_optimizer) {
        this.post_optimizer = post_optimizer;
    }
}
