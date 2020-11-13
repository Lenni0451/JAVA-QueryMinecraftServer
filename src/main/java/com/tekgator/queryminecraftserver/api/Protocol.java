package com.tekgator.queryminecraftserver.api;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public enum Protocol {

    TCP(47),                // query via TCP for every Minecraft version starting at 1.7.0 and above
    TCP_1_6(74),            // query via TCP for every Minecraft version 1.6.4 - 1.6.0
    TCP_1_5(61),            // query via TCP for every Minecraft version 1.5.2 - 1.4.0
    TCP_1_3(39),            // query via TCP for every Minecraft version 1.3.2 - 1.3.1
    TCP_1_2(29),            // query via TCP for every Minecraft version 1.2.5 - b1.8
    UDP_BASIC(1),           // basic information query via UDP
    UDP_FULL(2),            // full information query via UDP
    UDP_BEDROCK(0);         // ping bedrock servers

    private final int protocolId;

    Protocol(final int protocolId) {
        this.protocolId = protocolId;
    }

    public int getProtocolId() {
        return protocolId;
    }

}