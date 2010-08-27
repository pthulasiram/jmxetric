/*
 * Automatically generated by jrpcgen 1.0.5 on 10/23/08 8:11 PM
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package ganglia.xdr.v31x;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Ganglia_gmetric_double implements XdrAble {
    public Ganglia_metric_id metric_id;
    public String fmt;
    public double d;

    public Ganglia_gmetric_double() {
    }

    public Ganglia_gmetric_double(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        metric_id.xdrEncode(xdr);
        xdr.xdrEncodeString(fmt);
        xdr.xdrEncodeDouble(d);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        metric_id = new Ganglia_metric_id(xdr);
        fmt = xdr.xdrDecodeString();
        d = xdr.xdrDecodeDouble();
    }

}
// End of Ganglia_gmetric_double.java
