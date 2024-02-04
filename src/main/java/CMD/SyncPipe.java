package CMD;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

class SyncPipe implements Runnable
{
    public SyncPipe(InputStream istrm, PrintStream ostrm) {
        this.istrm = istrm;
        this.ostrm = ostrm;
    }

    public void run() {
        try
        {
            final byte[] buffer = new byte[1024];
            for (int length = 0; (length = this.istrm.read(buffer)) != -1; )
            {
                this.ostrm.write(buffer, 0, length);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private final OutputStream ostrm;
    private final InputStream istrm;
}