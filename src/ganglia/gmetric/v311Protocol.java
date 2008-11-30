package ganglia.gmetric;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.acplt.oncrpc.XdrBufferEncodingStream;

import ganglia.gmetric.GMetric.UDPAddressingMode;
import ganglia.xdr.v31x.Ganglia_extra_data;
import ganglia.xdr.v31x.Ganglia_gmetric_string;
import ganglia.xdr.v31x.Ganglia_metadata_message;
import ganglia.xdr.v31x.Ganglia_metadata_msg;
import ganglia.xdr.v31x.Ganglia_metadatadef;
import ganglia.xdr.v31x.Ganglia_metric_id;
import ganglia.xdr.v31x.Ganglia_msg_formats;
import ganglia.xdr.v31x.Ganglia_value_msg;

public class v311Protocol extends AbstractProtocol {
	private static final int MAX_BUFFER_SIZE = 1024 ;
    private XdrBufferEncodingStream xdr = new XdrBufferEncodingStream( MAX_BUFFER_SIZE );
    private Map<String,Integer> metricCounterMap = new HashMap<String, Integer>();
    private int metadataMessageInterval;

	public v311Protocol(String group, int port, UDPAddressingMode mode, 
			int metadataMessageInterval ) {
		super(group, port, mode);
		this.metadataMessageInterval = metadataMessageInterval ;
	}

	private boolean isTimeToSendMetadata( String metricName ) {
		boolean ret = false ;
        Integer counter = metricCounterMap.get(metricName);
        if (counter == null) {
            counter = 0;
            ret = true ;
        } else {
        	counter++;
        	if ( counter >= metadataMessageInterval ) {
        		counter = 0 ;
        		ret = true ;
        	}
        }
        metricCounterMap.put(metricName, counter);
        return ret ;
	}
	@Override
	public void announce( String name, String value,
			GMetricType type, String units, GMetricSlope slope, int tmax,
			int dmax, String groupName) throws Exception {

        Ganglia_metric_id metric_id = new Ganglia_metric_id();
        metric_id.host = InetAddress.getLocalHost().getHostName();
        metric_id.name = name;
        metric_id.spoof = false;

        if ( isTimeToSendMetadata( name ) ) {
            encodeGMetric( metric_id, name, value, type, units, slope, tmax, dmax, groupName );
            send(xdr.getXdrData(),xdr.getXdrLength());
        }
        encodeGValue( metric_id, value );
        send(xdr.getXdrData(),xdr.getXdrLength());
	}
    /**
     * Encodes the metric using the classes generated by remotetea
     */
    private void encodeGMetric(
            Ganglia_metric_id metric_id,
            String name, 
            String value, 
            GMetricType type,
            String units,
            GMetricSlope slope,
            int tmax,
            int dmax, 
            String groupName)
            throws Exception {
        Ganglia_metadata_message metadata_message = new Ganglia_metadata_message() ;
        Ganglia_extra_data[] extra_data_array = new Ganglia_extra_data[3];
        Ganglia_extra_data extra_data1 = new Ganglia_extra_data();
        extra_data_array[0] = extra_data1;
        extra_data1.name = "GROUP";
        extra_data1.data = groupName;
        Ganglia_extra_data extra_data2 = new Ganglia_extra_data();
        extra_data_array[1] = extra_data2;
        extra_data2.name = "TITLE";
        extra_data2.data = name;
        Ganglia_extra_data extra_data3 = new Ganglia_extra_data();
        extra_data_array[2] = extra_data3;
        extra_data3.name = "DESC";
        extra_data3.data = name;
             
        metadata_message.metadata = extra_data_array ;
        metadata_message.name = name ;
        metadata_message.type = type.getGangliaType() ;
        metadata_message.units = units ;
        metadata_message.slope = slope.getGangliaSlope() ;
        metadata_message.tmax = tmax ;
        metadata_message.dmax = dmax ;
        
        Ganglia_metadatadef metadatadef = new Ganglia_metadatadef();
        metadatadef.metric_id = metric_id;
        metadatadef.metric = metadata_message;
        
        Ganglia_metadata_msg metadata_msg = new Ganglia_metadata_msg();
        metadata_msg.id = Ganglia_msg_formats.gmetadata_full;
        metadata_msg.gfull = metadatadef;

        xdr.beginEncoding(udpAddr, port) ;
        metadata_msg.xdrEncode(xdr);
        xdr.endEncoding();
    }
    
    private void encodeGValue(Ganglia_metric_id metric_id, String value)
    	throws Exception {
        Ganglia_value_msg value_msg = new Ganglia_value_msg();
        value_msg.id = Ganglia_msg_formats.gmetric_string;
        Ganglia_gmetric_string str = new Ganglia_gmetric_string();
        str.str = value;
        str.metric_id = metric_id;
        str.fmt = "%s";
        value_msg.gstr = str;
        xdr.beginEncoding(udpAddr, port) ;
        value_msg.xdrEncode(xdr);
        xdr.endEncoding();
    }
}
