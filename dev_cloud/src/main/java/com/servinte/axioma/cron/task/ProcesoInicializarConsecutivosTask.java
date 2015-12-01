package com.servinte.axioma.cron.task;

import java.sql.Connection;

import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.mundo.ConsecutivosDisponibles;


public class ProcesoInicializarConsecutivosTask  implements Runnable{
	
	public void run() 
	{
		String nombre;
		String anioVigencia;
		int institucion;
		Connection con = null;
		
		try {
				//Para inicializar consecutivos de Informe Atencion Inicial de Urgencias
				ConsecutivosDisponibles mundoConsecutivos1 = new ConsecutivosDisponibles ();
				con = UtilidadBD.abrirConexion();
				boolean resultado1=true;
				nombre = "consecutivo_infor_atenc_inic_urg";
				int val1 = 1;
				String valor1= String.valueOf(val1);
				anioVigencia = "";
				institucion = 2;
				resultado1=mundoConsecutivos1.insertarModificarTrans(con, nombre, valor1, anioVigencia, institucion, false,"modificar");
				Log4JManager.info("resultado insercion1: "+resultado1);

				
				//Para inicializar consecutivos de Informe Inconsistencias en Verificacion de Bases de Datos
				ConsecutivosDisponibles mundoConsecutivos = new ConsecutivosDisponibles ();
				boolean resultado=true;
				nombre = "consecutivo_infor_incon_veribd";
				int val = 1;
				String valor= String.valueOf(val);
				anioVigencia = "";
				institucion = 2;
				resultado=mundoConsecutivos.insertarModificarTrans(con, nombre, valor, anioVigencia, institucion, false,"modificar");
				Log4JManager.info("resultado insercion2: "+resultado);
			
		}
		catch (Exception e) {
			//Si se llega a presentar un problema en la ejecución del método se cierra la conexión
			Log4JManager.error("Error en la inicializacion de consecutivos");
			
		}
		finally {
			UtilidadBD.closeConnection(con);
		}
		

	}
	

}
