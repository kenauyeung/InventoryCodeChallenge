package inventoryCodeChallenge.model;


import org.apache.commons.lang3.StringUtils;

public class RequestResponse<T> {
    public static enum State {
        SUCCESS,
        ERROR
    }

    private State state;
    private String message = "";
    private T data;

    public RequestResponse() {

    }

    public RequestResponse(State state, String message, T data) {
        this.state = state;
        this.message =  StringUtils.defaultIfBlank(message,"");
        this.data = data;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
