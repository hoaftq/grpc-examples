package org.example;

import io.grpc.Grpc;
import io.grpc.TlsChannelCredentials;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {

        // Path to file ca.pem
        var trustedCertFilePath = new File(args[0]);

        // Path to file service2.pem
        var certChainFilePath = new File(args[1]);

        // Path to file service2.key
        var privateKeyFilePath = new File(args[2]);

        var chanelCredentials = TlsChannelCredentials.newBuilder()
                .trustManager(trustedCertFilePath)
                .keyManager(certChainFilePath, privateKeyFilePath) // to use mutual SSL
                .build();

        var channel = Grpc.newChannelBuilderForAddress("localhost", 4000, chanelCredentials).build();

        var productClient = new ProductClient(channel);
        productClient.useBlockingStub();
        productClient.useStub();
        productClient.useFutureStub();

        var categoryClient = new CategoryClient(channel);
        categoryClient.executeWithBlockingStub();
        categoryClient.executeWithStub();
    }
}