package org.example.monitoring;

public enum HWPTASKS {
    HWTP_Get_WindowID{
        @Override
        public void poll(Object object){
            //Wirte implementation here
        }
    };

    public abstract void poll(Object object);
}
