package cz.zk.springmvcdemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class UdpConfig {

    private String channel = "inboundChannel";
    private Integer port = 7777;

    @Bean
    public MessageChannel inboundChannel() {
        return new DirectChannel();
    }

    @Bean(name = "udpReceivingAdapter7777")
    public UnicastReceivingChannelAdapter udpReceivingAdapter7777() {
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(7777);
        adapter.setOutputChannel(inboundChannel());
        adapter.setOutputChannelName("udpPort7777");
        return adapter;
    }

    @Bean(name = "udpReceivingAdapter8888")
    public UnicastReceivingChannelAdapter udpReceivingAdapter8888() {
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(8888);
        adapter.setOutputChannel(inboundChannel());
        adapter.setOutputChannelName("udpPort8888");
        return adapter;
    }

    /*
    @Bean
    public UnicastSendingMessageHandler udpSendingAdapter() {
        return new UnicastSendingMessageHandler("localhost", port);
    }
    */
}
