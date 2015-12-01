package util.clonacion;

import java.io.InputStream;

/**
 * Permite manejar un flujo de entrada de datos en memoria
 * @author Juan David Ramírez
 * @since 19 Agosto 2010
 * @version 1.0
 */
public class ByteArrayInputStreamRapido extends InputStream
{
	/**
	 * Buffer para el manejo de la clonación
	 */
	protected byte[] buf = null;

	/**
	 * N&uacute;mero de bytes que se pueden leer desde el buffer
	 */
	protected int count = 0;

	/**
	 * N&uacute;mero de bytes que ya se han leido desde el buffer
	 */
	protected int pos = 0;

	/**
	 * Constructor del buffer
	 * @param buf Array de bytes, contiene el objeto serializado
	 * @param count Número de bytes que se pueden leer desde el buffer
	 */
	public ByteArrayInputStreamRapido(byte[] buf, int count)
	{
		this.buf = buf;
		this.count = count;
	}

	@Override
	public final int available()
	{
		return count - pos;
	}

	@Override
	public final int read()
	{
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	@Override
	public final int read(byte[] b, int inicio, int cantidad)
	{
		if (pos >= count)
			return -1;

		if ((pos + cantidad) > count)
			cantidad = (count - pos);

		System.arraycopy(buf, pos, b, inicio, cantidad);
		pos += cantidad;
		return cantidad;
	}

	@Override
	public final long skip(long n)
	{
		if ((pos + n) > count)
			n = count - pos;
		if (n < 0)
			return 0;
		pos += n;
		return n;
	}

}
