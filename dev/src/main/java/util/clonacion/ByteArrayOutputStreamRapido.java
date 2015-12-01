package util.clonacion;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Permite manejar un flujo de salida de datos de la memoria
 * @author Juan David Ram&iacute;rez
 * @since 19 Agosto 2010
 * @version 1.0
 */
public class ByteArrayOutputStreamRapido extends OutputStream
{
	/**
	 * Buffer en el que se almacenar&aacute;n los datos
	 */
	protected byte[] buf = null;
	
	/**
	 * Tama&ntilde;o del buffer
	 */
	protected int size = 0;

	/**
	 * Construye un flujo de datos con un buffer de capacidad inicial de 5k
	 */
	public ByteArrayOutputStreamRapido()
	{
		this(5 * 1024);
	}

	/**
	 * Construye un flujo de datos con la capacidad inicial del buffer asignada
	 */
	public ByteArrayOutputStreamRapido(int initSize)
	{
		this.size = 0;
		this.buf = new byte[initSize];
	}

	/**
	 * Verificar que el buffer tenga la longitud necesaria.
	 * @param tamanio Tama&ntilde;o deseado del buffer
	 */
	private void verificarTamanoBuffer(int tamanio)
	{
		if (tamanio > buf.length)
		{
			byte[] old = buf;
			buf = new byte[Math.max(tamanio, 2 * buf.length)];
			System.arraycopy(old, 0, buf, 0, old.length);
			old = null;
		}
	}

	/**
	 * Obtener el tama&ntilde;o actual del buffer
	 * @return int Tama&ntilde;o del buffer.
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Obtener el array de bytes que contiene la información almacenada.
	 * @return byte[] Array de bytes, en casi todos los casos será mayor al tamaño del objeto serializado
	 */
	public byte[] getByteArray()
	{
		return buf;
	}

	@Override
	public final void write(byte b[])
	{
		verificarTamanoBuffer(size + b.length);
		System.arraycopy(b, 0, buf, size, b.length);
		size += b.length;
	}

	@Override
	public final void write(byte b[], int off, int len)
	{
		verificarTamanoBuffer(size + len);
		System.arraycopy(b, off, buf, size, len);
		size += len;
	}

	@Override
	public final void write(int b)
	{
		verificarTamanoBuffer(size + 1);
		buf[size++] = (byte) b;
	}

	/**
	 * Reinicializar el buffer
	 */
	public void reset()
	{
		size = 0;
	}

	/**
	 * retorna un {@link InputStream} para leer los datos almacenados en el buffer
	 * @return retorna Flujo de datos de lectura
	 */
	public InputStream getInputStream()
	{
		return new ByteArrayInputStreamRapido(buf, size);
	}

}
