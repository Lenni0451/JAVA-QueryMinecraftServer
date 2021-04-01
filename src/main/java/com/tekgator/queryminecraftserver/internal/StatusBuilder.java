package com.tekgator.queryminecraftserver.internal;

import com.google.gson.*;
import com.tekgator.queryminecraftserver.api.BedrockPong;
import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.QueryException;
import com.tekgator.queryminecraftserver.api.status.Status;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public class StatusBuilder {

    public static final String JSON_SERVER = "server";
    public static final String JSON_SERVER_HOSTNAME = "hostname";
    public static final String JSON_SERVER_TARGETHOSTNAME = "targethostname";
    public static final String JSON_SERVER_IPADDRESS = "ipaddress";
    public static final String JSON_SERVER_PORT = "port";
    public static final String JSON_SERVER_QUERYPORT = "queryport";
    public static final String JSON_SERVER_LATENCY = "latency";
    public static final String JSON_SERVER_SERVER_ID = "serverId";

    public static final String JSON_DESCRIPTION = "description";

    public static final String JSON_VERSION = "version";
    public static final String JSON_VERSION_NAME = "name";
    public static final String JSON_VERSION_PROTOCOL = "protocol";

    public static final String JSON_PLAYERS = "players";
    public static final String JSON_PLAYERS_MAX = "max";
    public static final String JSON_PLAYERS_ONLINE = "online";
    public static final String JSON_PLAYERS_SAMPLE = "sample";
    public static final String JSON_PLAYERS_SAMPLE_NAME = "name";
    public static final String JSON_PLAYERS_SAMPLE_ID = "id";

    public static final String JSON_MODINFO = "modinfo";
    public static final String JSON_MODINFO_TYPE = "type";
    public static final String JSON_MODINFO_MODLIST = "modList";
    public static final String JSON_MODINFO_MODLIST_MODID = "modid";
    public static final String JSON_MODINFO_MODLIST_VERSION = "version";

    public static final String JSON_GAMETYPE = "gametype";

    public static final String JSON_NINTENDO_LIMITED = "nintendoLimited";

    public static final String JSON_MAP = "map";

    public static final String JSON_FAVICON = "favicon";


    private static final Gson gson = new GsonBuilder().create();

    /**
     * Set this to true if you want to deserialize chat components
     * which are sent instead of a motd string
     */
    public static boolean DESERIALIZE_MOTD_COMPONENTS = false;

    private Protocol protocol;
    private ServerDNS serverDNS;
    private long latency;
    private String dataTcp;
    private byte[] dataUdp;

    public StatusBuilder setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public StatusBuilder setServerDNS(ServerDNS serverDNS) {
        this.serverDNS = serverDNS;
        return this;
    }

    public StatusBuilder setLatency(final long latency) {
        this.latency = latency;
        return this;
    }

    public StatusBuilder setData(String data) {
        this.dataTcp = data;
        return this;
    }

    public StatusBuilder setData(byte[] data) {
        this.dataUdp = data;
        return this;
    }

    public Status build() throws QueryException {
        Status status;

        switch (this.protocol) {
            case TCP_1_2:
            case TCP_1_3:
                status = buildTcpLegacy();
                break;
            case TCP_1_6:
            case TCP_1_5:
                status = buildTcpDepreciatedJson();
                break;
            case TCP:
                status = buildTcpJson();
                break;
            case UDP_BASIC:
                status = buildUdpBasic();
                break;
            case UDP_FULL:
                status = buildUdpFull();
                break;
            case UDP_BEDROCK:
                status = buildUdpBedrock();
                break;
            default:
                status = new Status();
                break;
        }

        return status;
    }

    private Status buildTcpLegacy() {
        JsonObject json = new JsonObject();
        JsonObject playersJson = new JsonObject();
        JsonObject versionJson = new JsonObject();
        json.addProperty(JSON_DESCRIPTION, this.dataTcp.split("§")[0]);

        playersJson.addProperty(JSON_PLAYERS_MAX, Integer.valueOf(this.dataTcp.split("§")[2]));
        playersJson.addProperty(JSON_PLAYERS_ONLINE, this.dataTcp.split("§")[1]);
        json.add(JSON_PLAYERS, playersJson);

        versionJson.addProperty(JSON_VERSION_NAME, "Unknown (Legacy)");
        versionJson.addProperty(JSON_VERSION_PROTOCOL, this.protocol.getProtocolId());
        json.add(JSON_VERSION, versionJson);

        addHostInfoToJson(json);

        return gson.fromJson(json, Status.class);
    }

    private Status buildTcpDepreciatedJson() {
        JsonObject json = new JsonObject();

        String[] strSplit = this.dataTcp.split("\0");

        json.add(JSON_VERSION, new JsonObject());
        json.add(JSON_PLAYERS, new JsonObject());

        for (int i = 0; i < strSplit.length; i++) {
            switch (i) {
                case 1:
                    try {
                        json.get(JSON_VERSION).getAsJsonObject().addProperty(JSON_VERSION_PROTOCOL, Integer.parseInt(strSplit[i].trim()));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                    break;

                case 2:
                    json.get(JSON_VERSION).getAsJsonObject().addProperty(JSON_VERSION_NAME, strSplit[i].trim());
                    break;

                case 3:
                    json.addProperty(JSON_DESCRIPTION, strSplit[i].trim());
                    break;

                case 4:
                case 5:
                    try {
                        json.get(JSON_PLAYERS).getAsJsonObject().addProperty(i == 4 ? JSON_PLAYERS_ONLINE : JSON_PLAYERS_MAX, Integer.parseInt(strSplit[i].trim()));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                    break;

                default:
                    break;
            }
        }

        addHostInfoToJson(json);

        return gson.fromJson(json, Status.class);
    }

    private Status buildTcpJson() throws QueryException {
        JsonObject json = new JsonObject();
        JsonElement jsonElem;

        try {
            jsonElem = JsonParser.parseString(this.dataTcp);
        } catch (JsonSyntaxException e) {
            jsonElem = JsonNull.INSTANCE;
        }

        if (jsonElem.isJsonNull()) {
            // invalid json object received
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, "Server returned invalid response!");
        } else if (jsonElem.isJsonPrimitive()) {
            // in case the server is just starting the data string contains only
            // a hint that is currently starting up
            json.addProperty(JSON_DESCRIPTION, jsonElem.getAsJsonPrimitive().getAsString());
        } else {
            json = jsonElem.getAsJsonObject();
        }

        if (json.has(JSON_DESCRIPTION) && json.get(JSON_DESCRIPTION).isJsonObject()) {
            // Some servers return their description as a chat component so we need to convert it to a text here
            jsonElem = json.remove(JSON_DESCRIPTION);
            if (DESERIALIZE_MOTD_COMPONENTS) {
                json.addProperty(JSON_DESCRIPTION, BasicComponentDeserializer.deserialize(jsonElem));
            } else {
                json.addProperty(JSON_DESCRIPTION, gson.toJson(jsonElem));
            }
        }

        addHostInfoToJson(json);

        return gson.fromJson(json, Status.class);
    }

    private Status buildUdpBasic() throws QueryException {
        JsonObject json = new JsonObject();

        ByteArrayInputStream b = new ByteArrayInputStream(this.dataUdp);
        DataInputStream d = new DataInputStream(b);

        json.add(JSON_VERSION, new JsonObject());
        json.add(JSON_PLAYERS, new JsonObject());

        for (int i = 1; i <= 5; i++) {
            switch (i) {
                case 1:
                    json.addProperty(JSON_DESCRIPTION, readNullTerminatedString(d));
                    break;
                case 2:
                    json.addProperty(JSON_GAMETYPE, readNullTerminatedString(d));
                    break;
                case 3:
                    json.addProperty(JSON_MAP, readNullTerminatedString(d));
                    break;
                case 4:
                case 5:
                    try {
                        json.get(JSON_PLAYERS).getAsJsonObject().addProperty(i == 4 ? JSON_PLAYERS_ONLINE : JSON_PLAYERS_MAX, Integer.parseInt(readNullTerminatedString(d)));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                    break;
                default:
                    break;
            }
        }

        addHostInfoToJson(json);

        return gson.fromJson(json, Status.class);
    }

    private Status buildUdpFull() throws QueryException {
        JsonObject json = new JsonObject();
        String key;
        String value;

        ByteArrayInputStream b = new ByteArrayInputStream(this.dataUdp);
        DataInputStream d = new DataInputStream(b);

        json.add(JSON_VERSION, new JsonObject());
        json.add(JSON_PLAYERS, new JsonObject());
        json.add(JSON_MODINFO, new JsonObject());

        while (b.available() > 0) {
            key = readNullTerminatedString(d);
            value = readNullTerminatedString(d);

            if (key.isEmpty() && value.equalsIgnoreCase("player_")) {
                byte[] streamRest = new byte[b.available()];

                try {
                    d.read(streamRest);
                } catch (IOException e) {
                    throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, "Server returned invalid response!");
                }

                readUdpPlayers(json, new String(streamRest));
            } else {
                if (key.equalsIgnoreCase("hostname")) {
                    json.addProperty(JSON_DESCRIPTION, value);
                } else if (key.equalsIgnoreCase("gametype")) {
                    json.addProperty(JSON_GAMETYPE, value);
                } else if (key.equalsIgnoreCase("version")) {
                    json.get(JSON_VERSION).getAsJsonObject().addProperty(JSON_VERSION_NAME, value);
                } else if (key.equalsIgnoreCase("plugins")) {
                    readUdpModInfo(json, value);
                } else if (key.equalsIgnoreCase("map")) {
                    json.addProperty(JSON_MAP, value);
                } else if (key.equalsIgnoreCase("numplayers") ||
                        key.equalsIgnoreCase("maxplayers")) {
                    try {
                        json.get(JSON_PLAYERS).getAsJsonObject().addProperty(key.equalsIgnoreCase("numplayers") ? JSON_PLAYERS_ONLINE : JSON_PLAYERS_MAX, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                } else if (key.equalsIgnoreCase("hostport")) {
                    try {
                        serverDNS.setPort(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        // invalid number, just ignore
                    }
                }
            }
        }

        addHostInfoToJson(json);

        return gson.fromJson(json, Status.class);
    }

    private void readUdpModInfo(final JsonObject json, String plugins) {
        int colonPos = plugins.indexOf(":");

        if (colonPos > 0) {
            json.get(JSON_MODINFO).getAsJsonObject().addProperty(JSON_MODINFO_TYPE, plugins.substring(0, colonPos).trim());
            plugins = plugins.substring(colonPos + 1).trim();

            JsonArray jsonModArray = new JsonArray();

            for (String s : plugins.split(";")) {
                JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty(JSON_MODINFO_MODLIST_MODID, s.trim());
                jsonModArray.add(jsonMod);
            }

            json.get(JSON_MODINFO).getAsJsonObject().add(JSON_MODINFO_MODLIST, jsonModArray);
        }
    }

    private void readUdpPlayers(final JsonObject json, final String players) {
        JsonArray jsonPlayerArray = new JsonArray();
        String playerName;

        for (String s : players.split("\0")) {
            playerName = s.trim();
            if (playerName.length() > 0) {
                JsonObject jsonPlayer = new JsonObject();
                jsonPlayer.addProperty(JSON_PLAYERS_SAMPLE_NAME, playerName);
                jsonPlayerArray.add(jsonPlayer);
            }
        }

        json.get(JSON_PLAYERS).getAsJsonObject().add(JSON_PLAYERS_SAMPLE, jsonPlayerArray);
    }

    private Status buildUdpBedrock() {
        BedrockPong pong = BedrockPong.fromRakNet(this.dataUdp);
        JsonObject json = new JsonObject();

        json.addProperty(JSON_DESCRIPTION, pong.getMotd() + "\n" + pong.getSubMotd());
        json.addProperty(JSON_GAMETYPE, pong.getGameType());
        json.addProperty(JSON_NINTENDO_LIMITED, pong.isNintendoLimited());
        {
            JsonObject versionJson = new JsonObject();
            versionJson.addProperty(JSON_VERSION_NAME, pong.getEdition() + " " + pong.getVersion());
            versionJson.addProperty(JSON_VERSION_PROTOCOL, pong.getProtocolVersion());

            json.add(JSON_VERSION, versionJson);
        }
        {
            JsonObject playersJson = new JsonObject();
            playersJson.addProperty(JSON_PLAYERS_MAX, pong.getMaximumPlayerCount());
            playersJson.addProperty(JSON_PLAYERS_ONLINE, pong.getPlayerCount());

            json.add(JSON_PLAYERS, playersJson);
        }
        {
            JsonObject serverJson = new JsonObject();
            serverJson.addProperty(JSON_SERVER_SERVER_ID, pong.getServerId());

            json.add(JSON_SERVER, serverJson);
        }

        addHostInfoToJson(json);

        return gson.fromJson(json, Status.class);
    }

    private String readNullTerminatedString(final DataInputStream dataInputStream) throws QueryException {
        byte byteRead;
        byte[] tmpData;

        try {
            tmpData = new byte[dataInputStream.available()];
            for (int i = 0; (byteRead = dataInputStream.readByte()) != 0; i++) {
                tmpData[i] = byteRead;
            }
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, "Server returned invalid response!");
        }

        return new String(tmpData).trim();
    }

    private void addHostInfoToJson(final JsonObject json) {
        JsonObject jsonObject = (json.has(JSON_SERVER) ? json.getAsJsonObject(JSON_SERVER) : new JsonObject());

        jsonObject.addProperty(JSON_SERVER_TARGETHOSTNAME, this.serverDNS.getTargetHostName());
        jsonObject.addProperty(JSON_SERVER_HOSTNAME, this.serverDNS.getHostName());
        jsonObject.addProperty(JSON_SERVER_IPADDRESS, this.serverDNS.getIpAddress());
        jsonObject.addProperty(JSON_SERVER_PORT, this.serverDNS.getPort());
        jsonObject.addProperty(JSON_SERVER_QUERYPORT, this.serverDNS.getQueryPort());
        jsonObject.addProperty(JSON_SERVER_LATENCY, this.latency);

        json.add(JSON_SERVER, jsonObject);
    }

}

