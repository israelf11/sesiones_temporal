package io.workshop.seguridad.codecserver;

import io.workshop.seguridad.codec.CustomPayloadCodec;

import java.io.IOException;
import java.util.Collections;

public class StartServer {

  public static void main(String[] args){
    try {
      System.out.println("Start...");
      new RDEHttpServer(Collections.singletonList(new CustomPayloadCodec()), 8081).start();
    }
    catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
    }
  }
  
}
