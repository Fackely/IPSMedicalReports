package util.Beans;


public class BeanPaciente
{
	int id_ingreso=0;
	String num_id ="";
	String tipo_id="";
	String fecha_nacimiento="";
	String estado_civil="";
	String sexo="";
	String primer_nombre="";
	String segundo_nombre="";
	String primer_apellido="";
	String segundo_apellido="";
	String direccion="";
	String telefono="";
	String email="";
	String ocupacion="";
	
	public String getNombreCompleto()
	{
		String nomC = "";
		nomC += this.primer_nombre;
		nomC += " ";
		nomC += this.segundo_nombre;
		nomC += " ";
		nomC += this.primer_apellido;
		nomC += " ";
		nomC += this.segundo_apellido;
		
		return nomC;
	}


	/**
	 * Sets the id_ingreso.
	 * @param id_ingreso The id_ingreso to set
	 */
	public void setId_ingreso(int id_ingreso)
	{
		this.id_ingreso = id_ingreso;
	}	
	
	/**
	 * Returns the direccion.
	 * @return String
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * Returns the email.
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the estado_civil.
	 * @return String
	 */
	public String getEstado_civil() {
		return estado_civil;
	}

	/**
	 * Returns the fecha_nacimiento.
	 * @return String
	 */
	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	/**
	 * Returns the num_id.
	 * @return String
	 */
	public String getNum_id() {
		return num_id;
	}

	/**
	 * Returns the primer_apellido.
	 * @return String
	 */
	public String getPrimer_apellido() {
		return primer_apellido;
	}

	/**
	 * Returns the primer_nombre.
	 * @return String
	 */
	public String getPrimer_nombre() {
		return primer_nombre;
	}

	/**
	 * Returns the segundo_apellido.
	 * @return String
	 */
	public String getSegundo_apellido() {
		return segundo_apellido;
	}

	/**
	 * Returns the segundo_nombre.
	 * @return String
	 */
	public String getSegundo_nombre() {
		return segundo_nombre;
	}

	/**
	 * Returns the sexo.
	 * @return String
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * Returns the telefono.
	 * @return String
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Returns the tipo_id.
	 * @return String
	 */
	public String getTipo_id() {
		return tipo_id;
	}

	/**
	 * Sets the direccion.
	 * @param direccion The direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Sets the email.
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the estado_civil.
	 * @param estado_civil The estado_civil to set
	 */
	public void setEstado_civil(String estado_civil) {
		this.estado_civil = estado_civil;
	}

	/**
	 * Sets the fecha_nacimiento.
	 * @param fecha_nacimiento The fecha_nacimiento to set
	 */
	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	/**
	 * Sets the num_id.
	 * @param num_id The num_id to set
	 */
	public void setNum_id(String num_id) {
		this.num_id = num_id;
	}

	/**
	 * Sets the primer_apellido.
	 * @param primer_apellido The primer_apellido to set
	 */
	public void setPrimer_apellido(String primer_apellido) {
		this.primer_apellido = primer_apellido;
	}

	/**
	 * Sets the primer_nombre.
	 * @param primer_nombre The primer_nombre to set
	 */
	public void setPrimer_nombre(String primer_nombre) {
		this.primer_nombre = primer_nombre;
	}

	/**
	 * Sets the segundo_apellido.
	 * @param segundo_apellido The segundo_apellido to set
	 */
	public void setSegundo_apellido(String segundo_apellido) {
		this.segundo_apellido = segundo_apellido;
	}

	/**
	 * Sets the segundo_nombre.
	 * @param segundo_nombre The segundo_nombre to set
	 */
	public void setSegundo_nombre(String segundo_nombre) {
		this.segundo_nombre = segundo_nombre;
	}

	/**
	 * Sets the sexo.
	 * @param sexo The sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * Sets the telefono.
	 * @param telefono The telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Sets the tipo_id.
	 * @param tipo_id The tipo_id to set
	 */
	public void setTipo_id(String tipo_id) {
		this.tipo_id = tipo_id;
	}
	
	/**
	 * Returns the ocupacion.
	 * @return String
	 */
	public String getOcupacion() {
		return ocupacion;
	}

	/**
	 * Sets the ocupacion.
	 * @param ocupacion The ocupacion to set
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	
	/**
	 * Returns the id_ingreso.
	 * @return int
	 */
	public int getId_ingreso() {
		return id_ingreso;
	}

	
	public void clean()
	{
		this.num_id = "";
		this.tipo_id = "";
		this.fecha_nacimiento = "";
		this.estado_civil = "";
		this.sexo = "";
		this.primer_nombre = "";
		this.segundo_nombre = "";
		this.primer_apellido = "";
		this.segundo_apellido = "";
		this.direccion = "";
		this.telefono = "";
		this.email = "";
	}
}