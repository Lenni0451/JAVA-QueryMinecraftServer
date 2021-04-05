package com.tekgator.queryminecraftserver.internal;

import com.tekgator.queryminecraftserver.api.QueryException;
import org.xbill.DNS.*;

import java.net.UnknownHostException;
import java.util.regex.Pattern;


/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public class ServerDNS {

    private static final String IP4_PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static String DNS_IP = null;
    private static final int DEFAULT_PORT = 25565;
    private static final String SRV_STR = "_minecraft._tcp.";

    private String ipAddress = "";
    private String targetHostName = "";
    private String hostName = "";
    private int port = 0;
    private int queryPort = 0;

    public ServerDNS(final String hostName) throws QueryException {
        this(hostName, DEFAULT_PORT);
    }

    public ServerDNS(final String hostName, final int port) throws QueryException {
        this(hostName, port, true);
    }

    public ServerDNS(final String hostName, final int port, final boolean srvLookup) throws QueryException {
        resolve(hostName, port, srvLookup);
    }

    private void resolve(final String hostName, final int port, final boolean srvLookup) throws QueryException {
        this.targetHostName = hostName;
        this.hostName = this.targetHostName;
        this.port = port;

        if (srvLookup && !Pattern.compile(IP4_PATTERN).matcher(this.targetHostName).matches() && (this.port == 0 || this.port == 25565)) {
            // input is an hostname, but no port submitted, try to resolve via SRV record
            try {
                SRVRecord srvRecord = (SRVRecord) lookupRecord(SRV_STR + hostName, Type.SRV);

                this.targetHostName = srvRecord.getTarget().toString().replaceFirst("\\.$", "");
                this.port = srvRecord.getPort();
            } catch (QueryException e) {
                // no SRV record found at the moment, just continue
            }
        }

        if (Pattern.compile(IP4_PATTERN).matcher(this.targetHostName).matches()) {
            // hostname provided is a IP address
            this.ipAddress = this.targetHostName;
        } else {
            // hostname provided is an actual hostname, resolve IP address
            this.ipAddress = ((ARecord) lookupRecord(this.targetHostName, Type.A)).getAddress().getHostAddress();
        }

        if (this.port == 0) {
            // couldn't resolve via SVR record, therefore use default minecraft port
            this.port = DEFAULT_PORT;
        }

        setQueryPort(this.port); // this will raise an exception in case the supplied port is out of range
    }

    private Record lookupRecord(final String hostName, final int type) throws QueryException {
        Record record;
        Lookup lookup;
        int result;

        try {
            lookup = new Lookup(hostName, type);
            if (DNS_IP != null) lookup.setResolver(new SimpleResolver(DNS_IP));
        } catch (TextParseException | UnknownHostException e) {
            throw new QueryException(QueryException.ErrorType.HOST_NOT_FOUND, String.format("Host '%s' parsing error:%s", hostName, e.getMessage()));
        }

        lookup.run();

        result = lookup.getResult();

        if (result == Lookup.SUCCESSFUL) {
            record = lookup.getAnswers()[0];
        } else {
            switch (result) {
                case Lookup.HOST_NOT_FOUND:
                    throw new QueryException(QueryException.ErrorType.HOST_NOT_FOUND, String.format("Host '%s' not found", hostName));
                case Lookup.TYPE_NOT_FOUND:
                    throw new QueryException(String.format("Host '%s' not found (no A record)", hostName));
                case Lookup.UNRECOVERABLE:
                    throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, String.format("Cannot lookup host '%s'", hostName));
                case Lookup.TRY_AGAIN:
                    throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, String.format("Temporary failure to lookup host '%s'", hostName));
                default:
                    throw new QueryException(String.format("Unknown error %d in host lookup of '%s'", result, hostName));
            }
        }

        return record;
    }

    private int validatePort(final int port) throws IllegalArgumentException {
        if (port == 0 || port > Math.pow(2, 16) - 1) {
            throw new IllegalArgumentException(String.format("Port is out of valid range: %d", port));
        }
        return port;
    }

    public String getTargetHostName() {
        return this.targetHostName;
    }

    public String getHostName() {
        return this.hostName;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(final int port) throws IllegalArgumentException {
        this.port = validatePort(port);
    }

    public int getQueryPort() {
        return this.queryPort;
    }

    public void setQueryPort(final int queryPort) throws IllegalArgumentException {
        this.queryPort = validatePort(queryPort);
    }

}
