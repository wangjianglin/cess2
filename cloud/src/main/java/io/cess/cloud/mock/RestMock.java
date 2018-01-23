package io.cess.cloud.mock;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;

import java.net.URI;

public interface RestMock {

    default Object mock(URI uri, HttpMethod method){
        return null;
    }

    default Object mock(ClientHttpRequest request){
        return null;
    }
    Null NULL_VALUE = new Null();

    class Null{
        private Null(){}
    }

    class Exception extends RuntimeException{

        private Object value;
        public Exception(Object value){
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}
