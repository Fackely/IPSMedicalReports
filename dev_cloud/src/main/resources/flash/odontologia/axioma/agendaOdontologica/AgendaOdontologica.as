package axioma.agendaOdontologica {
	
	import flash.display.MovieClip;
	import flash.events.KeyboardEvent;
	import flash.display.Sprite;
	import flash.external.ExternalInterface;
	import flash.geom.Rectangle;	
	import flash.text.TextField;
	import flash.text.TextFieldType;
    import flash.text.TextFormat;
	import flash.display.CapsStyle;
	import flash.display.LineScaleMode;
	import flash.display.JointStyle;
	import flash.text.AntiAliasType;
    import flash.text.TextFieldAutoSize;
	import flash.events.MouseEvent;
	import flash.events.KeyboardEvent;

	import fl.events.ScrollEvent;
	import fl.containers.ScrollPane;
	import fl.controls.ScrollPolicy;
	import fl.controls.ScrollBarDirection;
	import fl.controls.Button;
	
	import flash.ui.Keyboard;
	
	import axioma.estructuras.Lista;
	import axioma.comun.EspacioTiempo;
	import axioma.comun.Consultorio;
	
	import util.general.Constantes;
	import util.general.UtilidadBoolean;

	public class AgendaOdontologica extends MovieClip{
		
		// Constants:
		// Public Properties:
		// Private Properties:
		private var xmlAgenda:String = "" ;
		private var xmlConsultorios:String = "" ;
		private var xmlParametros:String = "" ;	
		
		private var formatMensajes:TextFormat;
		
		private var anchoEscenario:int = 0;
		private var largoEscenario:int = 0;
		private var espacioEntreElementos:int = 2;
		
		private var fondo:Sprite;
		private var horarios:Sprite;
		private var consultorios:Sprite;
		private var convenciones:Sprite;

		private var colorHorario:String = "0x00F0F0";

		private var anchoInfo:int = 0; //Este se calcula
		private var altoInfo:int = 0;
		private var bordeInfo:int = 5;
		private var info:Sprite;
		
		var scrollHoraActual:int=0;
		
		var popup:Sprite = null;

		private var mousePresionado:Boolean = false;

		private var consultorioSeleccionado:Consultorio;
		
		private var scrollSize:int = 23;
		
		var error:Sprite = null;
		
		private var actividad:String = null;
		
		private static var DIALOGO="dialogo";
		
		private static var BOTON_NO:String="boton_no";

		private static var BOTON_SI:String="boton_si";

		public function AgendaOdontologica(){
			// este xml es de prueba
			var convencionesXML:String = 
				'<generacion>'+
					'<convenciones>'+
						'<convencion><nombre>REHABILITACIÓN ORAL NO SE QUE TAN LARGO ES</nombre><color>#F12546</color></convencion>'+
						'<convencion><nombre>REHABILITACIÓN ORAL NO SE QUE TAN LARGO ES</nombre><color>#F12546</color></convencion>'+
						'<convencion><nombre>REHABILITACIÓN ORAL NO SE QUE TAN LARGO ES</nombre><color>#F12546</color></convencion>'+
						'<convencion><nombre>Periodoncia</nombre><color>#F6A87C</color></convencion>'+
						'<convencion><nombre>Periodoncia</nombre><color>#F6A87C</color></convencion>'+
						'<convencion><nombre>Exodoncia</nombre><color>#0FA07C</color></convencion>'+
						'<convencion><nombre>Exodoncia</nombre><color>#0FA07C</color></convencion>'+
					'</convenciones>'+
					'<consultorios>'+
						'<consultorio>'+
							'<numero>C001 Consultorio radiología</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true" permiteCupoExtra="true">'+
								'<codigo_pk>1</codigo_pk>'+
								'<color>#F12546</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>08:00</hora>'+
										'<cuposExtra>2</cuposExtra>'+
										'<profesional>Juan David Ramírez</profesional>'+
										'<duracion>60</duracion>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>08:20</hora>'+
										'<profesional>Juan David Ramírez</profesional>'+
										'<especialidad>Anestesiólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>08:40</hora>'+
										'<profesional>Juan David Ramírez</profesional>'+
										'<especialidad>Anestesiólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>09:00</hora>'+
										'<profesional>Juan David Ramírez</profesional>'+
										'<especialidad>Anestesiólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>09:20</hora>'+
										'<profesional>Juan David Ramírez</profesional>'+
										'<especialidad>Anestesiólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
								'<citas>'+
									'<cita>'+
										'<estado>Asignada</estado>'+
										'<tipo>Valoracion Inicial</tipo>'+
										'<plan>Activo</plan>'+
										'<hora_inicio>08:00</hora_inicio>'+
										'<hora_fin>09:00</hora_fin>'+
										'<duracion>40</duracion>'+
										'<paciente>'+
											'<nombre>Pepe Pérez Prado</nombre>'+
											'<tipoID>CC</tipoID>'+
											'<numeroID>123456</numeroID>'+
										'</paciente>'+
										'<servicios>'+
											'<servicio>'+
												'<nombre>Amalgama</nombre>'+
												'<condiciones>'+
													'<condicion>'+
														'Ayunas'+
													'</condicion>'+
													'<condicion>'+
														'Lavarse los dientes'+
													'</condicion>'+
												'</condiciones>'+
											'</servicio>'+
											'<servicio>'+
												'<nombre>Este servicio es graaaande</nombre>'+
											'</servicio>'+
										'</servicios>'+
									'</cita>'+
									'<cita>'+
										'<estado>Asignada</estado>'+
										'<tipo>1</tipo>'+
										'<plan>No se</plan>'+
										'<hora_inicio>08:00</hora_inicio>'+
										'<hora_fin>09:00</hora_fin>'+
										'<duracion>20</duracion>'+
										'<paciente>'+
											'<nombre>Otro Mancito</nombre>'+
											'<tipoID>CC</tipoID>'+
											'<numeroID>654321</numeroID>'+
										'</paciente>'+
										'<servicios>'+
											'<servicio>'+
												'<nombre>Valoración</nombre>'+
											'</servicio>'+
										'</servicios>'+
									'</cita>'+
								'</citas>'+
							'</generadas>'+
							'<generadas permiteAsignar="true" permiteReservar="true">'+
								'<codigo_pk>2</codigo_pk>'+
								'<color>#F12548</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>10:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>10:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>10:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>11:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>11:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>11:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
							'</generadas>'+
						'</consultorio>'+
						
						
						
						'<consultorio>'+
							'<numero>C002</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true">'+
								'<codigo_pk>3</codigo_pk>'+
								'<color>#F6A87C</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
							'</generadas>'+
						'</consultorio>'+
						'<consultorio>'+
							'<numero>C002</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true">'+
								'<codigo_pk>3</codigo_pk>'+
								'<color>#F6A87C</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
							'</generadas>'+
						'</consultorio>'+
						'<consultorio>'+
							'<numero>C002</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true">'+
								'<codigo_pk>3</codigo_pk>'+
								'<color>#F6A87C</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
							'</generadas>'+
						'</consultorio>'+
						'<consultorio>'+
							'<numero>C002</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true">'+
								'<codigo_pk>3</codigo_pk>'+
								'<color>#F6A87C</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
							'</generadas>'+
						'</consultorio>'+
						'<consultorio>'+
							'<numero>C002</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true">'+
								'<codigo_pk>3</codigo_pk>'+
								'<color>#F6A87C</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
							'</generadas>'+
						'</consultorio>'+
						'<consultorio>'+
							'<numero>C002</numero>'+
							'<generadas permiteAsignar="true" permiteReservar="true" permiteCupoExtra="true">'+
								'<codigo_pk>3</codigo_pk>'+
								'<color>#F6A87C</color>'+
								'<cupos>'+
									'<cupo>'+
										'<hora>12:00</hora>'+
										'<cuposExtra>2</cuposExtra>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:20</hora>'+
										'<cuposExtra>2</cuposExtra>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>12:40</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:00</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
									'<cupo>'+
										'<hora>13:20</hora>'+
										'<profesional>Carolina Betancourt Uribe</profesional>'+
										'<especialidad>Odontólogo</especialidad>'+
									'</cupo>'+
								'</cupos>'+
								'<citas>'+
									'<cita>'+
										'<estado>Asignada</estado>'+
										'<tipo>Valoracion Inicial</tipo>'+
										'<plan>Activo</plan>'+
										'<hora_inicio>12:00</hora_inicio>'+
										'<hora_fin>12:20</hora_fin>'+
										'<duracion>20</duracion>'+
										'<paciente>'+
											'<nombre>Pepe Pérez Prado</nombre>'+
											'<tipoID>CC</tipoID>'+
											'<numeroID>123456</numeroID>'+
										'</paciente>'+
										'<servicios>'+
											'<servicio>'+
												'<nombre>Amalgama</nombre>'+
												'<condiciones>'+
													'<condicion>'+
														'Ayunas'+
													'</condicion>'+
													'<condicion>'+
														'Lavarse los dientes'+
													'</condicion>'+
												'</condiciones>'+
											'</servicio>'+
											'<servicio>'+
												'<nombre>Este servicio es graaaande</nombre>'+
											'</servicio>'+
										'</servicios>'+
									'</cita>'+
								'</citas>'+
							'</generadas>'+
						'</consultorio>'+
						
					'</consultorios>'+
					'<agenda>'+
						'<intervalo>20</intervalo>'+
						'<horaInicio>8</horaInicio><minInicio>0</minInicio>'+
						'<horaFin>20</horaFin>'+
						'<minFin>0</minFin>'+
						'<horaActual>08:00</horaActual>'+
						'<horaPosibleAsignacion>08:00</horaPosibleAsignacion>'+
//						'<actividad>ASIGN</actividad>'+
//						'<actividad>RESER</actividad>'+
						'<actividad></actividad>'+
					'</agenda>'+
				'</generacion>';
				
			//var prueba:String="";
//			asignarXML(convencionesXML);
//			actividad=Constantes.actividadAsignar;
//			asignarXML(prueba);
//			asignarXML("");
			ExternalInterface.addCallback("asignarXML",asignarXML);

		}

		public function asignarXML(creacionAgendaStr:String):int
		{
			var creacionAgendaXML:XML = new XML(creacionAgendaStr);
			var convencionesXML:XML = creacionAgendaXML.convenciones[0];
			var agendaXML:XML = creacionAgendaXML.agenda[0];
			var consultoriosXML:XML = creacionAgendaXML.consultorios[0];
			
			/* Remuevo todos los elementos adicionales */
			var cantidad=0;
			while(this.numChildren > cantidad)
			{
				if(this.getChildAt(cantidad).name.substr(0,6)=="scroll")
				{
					cantidad++;
				}
				else
				{
					this.removeChildAt(cantidad);
				}
			}
			scrollHoras.source=null;
			scrollPane.source=null;
			scrollConsultorios.source=null;
			scrollConvenciones.source=null;

			if(creacionAgendaStr=="")
			{
				var formatError = new TextFormat();
				formatError.color = 0x000000;
				formatError.size = 15;
				formatError.bold = true;
				formatError.italic = false;
				formatError.font = "Arial";

				error = crearCelda(100, 50, 600, 30, 10, '0x3299FF', "             No hay información con los parámetros de búsqueda seleccionados", formatError);
				this.addChild(error);
				return -1;
			}
			
			
			anchoEscenario = 700;
			largoEscenario = 450;

			fondo = new Sprite();
			horarios = new Sprite();
			consultorios = new Sprite();
			convenciones = new Sprite();

			// Tamaños
			scrollPane.width=770;
			scrollPane.height=333;
			
			scrollPane.x=70;
			
			scrollConsultorios.width=755;
			scrollConsultorios.height=25;
			
			scrollConsultorios.x=70;
			
			scrollHoras.width=70;
			scrollHoras.height=318;

			scrollConvenciones.width=840;
			scrollConvenciones.height=50;

			scrollConvenciones.x=0;
			scrollConvenciones.y=30;

			inicializarConvenciones(XML(convencionesXML));
			
			inicializarHorarios(XML(agendaXML), XML(consultoriosXML));
			
			scrollPane.source=fondo;
			scrollPane.addEventListener(ScrollEvent.SCROLL, scrollEvent);

			scrollPane.verticalLineScrollSize=scrollSize;

			scrollHoras.source=horarios;
			
			scrollConsultorios.source=consultorios;
			
			scrollHoras.verticalScrollPolicy=ScrollPolicy.OFF;
			scrollHoras.horizontalScrollPolicy=ScrollPolicy.OFF;
			scrollConsultorios.verticalScrollPolicy=ScrollPolicy.OFF;
			scrollConsultorios.horizontalScrollPolicy=ScrollPolicy.OFF;
			
			if(scrollHoraActual>0)
			{
				scrollHoras.verticalScrollPosition = scrollHoraActual;
				scrollPane.verticalScrollPosition = scrollHoraActual;
			}

			formatMensajes = new TextFormat();
			formatMensajes.color = 0x000000;
            formatMensajes.size = 11;
            formatMensajes.bold = true;
            formatMensajes.italic = false;
			formatMensajes.font = "Arial";
			
			this.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
			this.addEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
			this.addEventListener(MouseEvent.MOUSE_UP, mouseUp);
			this.addEventListener(MouseEvent.MOUSE_WHEEL, mouseWheel);
			
			this.addEventListener(KeyboardEvent.KEY_DOWN, keyDown);
			
			organizarScroll();

			return 0;
		}
		
		private function inicializarHorarios(xmlAgenda:XML, xmlConsulorios:XML):void
		{
			// Formato de la letra
			var formatAgenda:TextFormat = new TextFormat();
			formatAgenda.color = 0x000000;
			formatAgenda.font = "Arial";
			
			var y0:int = 80;
			var posicionX:int = 15;
			var posicionY:int = y0;
			var ancho:int = 50;
			var alto:int = 25;
			var borde:int = 0;
			var intervalo:String;
			var horaInicio:String;
			var minutoInicio:String;
			var horaFin:String;
			var minutoFin:String;
			var espacioCeldas:int = 2;

			intervalo=xmlAgenda.intervalo;
			horaInicio=xmlAgenda.horaInicio;
			minutoInicio=xmlAgenda.minInicio;
			horaFin=xmlAgenda.horaFin;
			minutoFin=xmlAgenda.minFin;
			if(xmlAgenda.actividad=='')
			{
				actividad=null;
			}
			else
			{
				actividad=xmlAgenda.actividad;
			}
			
			if(intervalo==null)
			{
				trace("Error en el XML");
				return;
			}

			var hora:Date = new Date();
			var horaLimite:Date = new Date();
			hora.setHours(Number(horaInicio), Number(minutoInicio), Number(0));
			horaLimite.setHours(Number(horaFin), Number(minutoFin), Number(0));
			
			formatAgenda.size = 12;
			formatAgenda.bold = true;
			var celda:Sprite = crearCelda(posicionX, y0, ancho, alto, borde, colorHorario, "HORA", formatAgenda);
			this.addChild(celda);
			
			posicionY=0;

			formatAgenda.size = 13;
			formatAgenda.bold = false;
			var numeroHorarios:int = 0;
			
			var listaHorarios:Lista=new Lista();
			var listaHorariosFin:Lista=new Lista();

			// Creación de las horas
			for(var i=0; hora.getTime()<horaLimite.getTime(); i++)
			{
				var textoHora:String = "";
				
				var hor:int = hora.getHours();
				var min:int = hora.getMinutes();
				textoHora+=hor<10?"0"+hor:hor;
				textoHora+=":";
				textoHora+=min<10?"0"+min:min;

				var textoHoraFin:String = "";
				
				var horaFinal:Date = new Date();
				horaFinal.setTime(hora.getTime()+(Number(intervalo)*60*1000)-1);
				
				hor = horaFinal.getHours();
				min = horaFinal.getMinutes();
				textoHoraFin+=hor<10?"0"+hor:hor;
				textoHoraFin+=":";
				textoHoraFin+=min<10?"0"+min:min;

				if(xmlAgenda.horaActual>=textoHora && xmlAgenda.horaActual<textoHoraFin)
				{
					scrollHoraActual=posicionY;
				}

				listaHorarios.add(textoHora);
				listaHorariosFin.add(textoHoraFin);
				
				celda = crearCelda(posicionX, posicionY, ancho, alto, borde, colorHorario, textoHora, formatAgenda);
				
				horarios.addChild(celda);

				hora.setTime(hora.getTime()+(Number(intervalo)*60*1000));
				posicionY+=alto+espacioCeldas;
				numeroHorarios++;
			}
			var anchoConsultorio:int = 120;
			var posXCita=0;
			var posXCitaEnScrollPane:int = 0;
			
			// Creación de los consultorios
			var numeroConsultorio:int = 0;
			for each(var consul:XML in xmlConsulorios.elements())
			{
				posicionY=0;
				formatAgenda.size = 11;
				formatAgenda.bold = false;
				var consultorioStr:String = consul.numero;
				if(consultorioStr.length > 15)
				{
					consultorioStr=consultorioStr.substr(0, 15);
				}
				celda = crearCelda(posXCita, posicionY, anchoConsultorio, alto, borde, colorHorario, consultorioStr, formatAgenda);
				consultorios.addChild(celda);
				
				var consultorio:Consultorio = new Consultorio();
				consultorio.setIndice(numeroConsultorio);
				
				for(var j=0; j<numeroHorarios; j++)
				{
					var espacio:EspacioTiempo;
					var cupo:XML = contiene(listaHorarios.get(j), consul.generadas);
					if(cupo!=null)
					{
						var celdasOcupadas=1;
						var altoCelda=alto;
						if(cupo.duracion>0)
						{
							celdasOcupadas=(cupo.duracion)/Number(intervalo);
							altoCelda=(celdasOcupadas * alto) + ( (celdasOcupadas-1) * espacioCeldas);
						}
						espacio = EspacioTiempo(crearEspacioTiempo(posXCitaEnScrollPane, posicionY, anchoConsultorio, altoCelda, borde, cupo.color, "", formatAgenda, numeroConsultorio));
						posicionY+=alto+espacioCeldas;
						espacio.setHora(cupo.hora);
						espacio.setHoraFin(String(listaHorariosFin.get(j)));
						espacio.setProfesional(cupo.profesional);
						espacio.setEspecialidad(cupo.especialidad);
						espacio.setConsultorio(consultorio);
						espacio.setCodigoAgenda(cupo.codigo_pk);

						consultorio.addEspacio(espacio);
						espacio.addEventListener(MouseEvent.MOUSE_OVER, mouseOver);
						espacio.addEventListener(MouseEvent.MOUSE_OUT, mouseOut);
						fondo.addChild(espacio);
						if(cupo.duracion>0)
						{
							j+=celdasOcupadas-1;
							posicionY+=(alto+espacioCeldas)*(celdasOcupadas-1);
							espacio.setCupoExtra(true);
						}
					}
					else{
						espacio = EspacioTiempo(crearEspacioTiempo(posXCitaEnScrollPane, posicionY, anchoConsultorio, alto, borde, "0xEEEEEE", "", formatAgenda, numeroConsultorio));
						espacio.setHabilitado(false);
						posicionY+=alto+espacioCeldas;
						espacio.setConsultorio(consultorio);
						consultorio.addEspacio(espacio);
						fondo.addChild(espacio);
					}
					if(listaHorarios.get(j)<xmlAgenda.horaPosibleAsignacion)
					{
						espacio.alpha=0.4;
						espacio.setHabilitado(false);
					}
					var citas:XMLList = adicionarCitas(listaHorarios.get(j), consul.generadas);
					if(citas!= null && citas.length()>0)
					{
						var laberEspacio:String="PTES AGENDADOS: "+citas.child("paciente").length();
						formatAgenda.size=10;
						adicionarLabelEspacioTiempoCoordenadas(espacio, laberEspacio, formatAgenda, 4, 4);
						formatAgenda.size=12;
						espacio.setCita(citas);
						espacio.setHoraFin(citas[0].hora_fin);
						espacio.setAsignado(true);
						espacio.alpha=0.5;
						if(cupo.cuposExtra>0)
						{
							espacio.setPermiteCupoExtra(UtilidadBoolean.convertirStringABoolean(cupo.@permiteCupoExtra));
							espacio.setPermiteAsignar(UtilidadBoolean.convertirStringABoolean(cupo.@permiteAsignar));
							espacio.setCuposExtraDisponibles(cupo.cuposExtra);
							if(listaHorarios.get(j)>xmlAgenda.horaActual)
							{
								espacio.setPermiteReservar(UtilidadBoolean.convertirStringABoolean(cupo.@permiteReservar));
							}
							else
							{
								espacio.setPermiteReservar(false);
							}
						}
					}
					else
					{
						if(cupo!=null)
						{
							espacio.setPermiteAsignar(UtilidadBoolean.convertirStringABoolean(cupo.@permiteAsignar));
							if(listaHorarios.get(j)>xmlAgenda.horaActual)
							{
								espacio.setPermiteReservar(UtilidadBoolean.convertirStringABoolean(cupo.@permiteReservar));
							}
							else
							{
								espacio.setPermiteReservar(false);
							}
						}
					}
				}
				posXCita+=anchoConsultorio+espacioCeldas;
				posXCitaEnScrollPane+=anchoConsultorio+espacioCeldas;
				numeroConsultorio++;
			}
		}

		private function crearEspacioTiempo(x:int, y:int, ancho:int, alto:int, borde:int, color:String, texto:String, formatAgenda:TextFormat, indiceConsultorio:int):Sprite
		{
			var espacioTiempo:EspacioTiempo = new EspacioTiempo();
			
			asignarPropiedadesSprite(espacioTiempo, x, y, ancho, alto, borde, color, texto, formatAgenda);
			return espacioTiempo;
		}

		private function crearCelda(x:int, y:int, ancho:int, alto:int, borde:int, color:String, texto:String, formatAgenda:TextFormat):Sprite
		{
			var celda:Sprite = new Sprite();
			asignarPropiedadesSprite(celda, x, y, ancho, alto, borde, color, texto, formatAgenda);
			return celda;
		}

		private function asignarPropiedadesSprite(celda:Sprite, x:int, y:int, ancho:int, alto:int, borde:int, color:String, texto:String, formatAgenda:TextFormat):void
		{
			celda.x=x;
			celda.y=y;
			celda.graphics.beginFill(uint(color));
			celda.graphics.lineStyle(1,uint(color),0.1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			celda.graphics.drawRoundRect(0, 0, ancho, alto, borde, borde);
			celda.graphics.endFill();
			celda.alpha= 0.9;
			
			// se adiciona el texto de la hora
			adicionarLabelEspacioTiempo(celda, texto, formatAgenda);
			
//			return celda;
		}

		private function adicionarLabelEspacioTiempo(celda:Sprite, texto:String, formatAgenda:TextFormat):void
		{
			var top=3;
			var left=7;
			adicionarLabelEspacioTiempoCoordenadas(celda, texto, formatAgenda, left, top);
		}

		private function adicionarLabelEspacioTiempoCoordenadas(celda:Sprite, texto:String, formatAgenda:TextFormat, left:int, top:int):void
		{
			var labelHora:TextField = new TextField();
			labelHora.defaultTextFormat = formatAgenda;
			labelHora.selectable = false;
			labelHora.mouseEnabled = false;
			labelHora.autoSize = TextFieldAutoSize.CENTER;
			labelHora.antiAliasType = AntiAliasType.NORMAL; 
			labelHora.border = false;
			labelHora.text = texto;
			labelHora.x = left;
			labelHora.y = top;

			celda.addChild(labelHora);
		}
		
		private function crearMenuOpciones(x:int, y:int, ancho:int, alto:int, borde:int, color:String, formatAgenda:TextFormat):Sprite
		{
			var celda:Sprite=new Sprite();
			celda.x=x;
			celda.y=y;
			celda.graphics.beginFill(uint(color));
			celda.graphics.lineStyle(3,uint(0x000000),0.4,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			celda.graphics.drawRoundRect(0, 0, ancho, alto, borde, borde);
			celda.graphics.endFill();
			celda.name=DIALOGO;
			celda.addEventListener(MouseEvent.MOUSE_OUT, cerrarPopupAsignacionReserva);
			return celda;
		}

		
		private function inicializarConvenciones(xmlConvenciones:XML):void
		{
			// definición de la letra
			var formatAgenda:TextFormat = new TextFormat();
			formatAgenda.color = 0x000000;
			formatAgenda.size = 15;
			formatAgenda.bold = true;
			formatAgenda.font = "Arial";
			formatAgenda.italic = false;
			
			var posicionX:int = 40;
			var y0:int = 0;
			var ancho:int = 30;
			var alto:int = 30;
			var borde:int = 10;
			var maxLetras:int =25;
			var espacioParaNombre:int =maxLetras*6.6;

			// Titulo
			var labelTituloConvencion:TextField = new TextField();
			labelTituloConvencion.defaultTextFormat = formatAgenda;
			labelTituloConvencion.selectable = false;
			labelTituloConvencion.mouseEnabled = false;
			labelTituloConvencion.autoSize = TextFieldAutoSize.CENTER;
			labelTituloConvencion.antiAliasType = AntiAliasType.NORMAL; 
			labelTituloConvencion.border = false;
			labelTituloConvencion.text = "Convenciones:";
			labelTituloConvencion.x = 10;
			labelTituloConvencion.y = 5;
			this.addChild(labelTituloConvencion);

			formatAgenda.color = 0x000000;
			formatAgenda.size = 10;
			formatAgenda.bold = true;
			formatAgenda.font = "Arial";
			formatAgenda.italic = false;

			for each(var nodo:XML in xmlConvenciones.elements())
			{
				var col:String = nodo.color;
				col = "0x"+col.substr(1, col.length);
				var celda:Sprite = crearCelda(posicionX, y0, ancho, alto, borde, col, "", formatAgenda);
				
				//
				
				var nombre:String = nodo.nombre;
				if(nombre.length>15)
				{
					nombre=nombre.substring(0, maxLetras);
				}

				var labelConvencion:Sprite = crearCelda(posicionX+ancho, y0, espacioParaNombre, alto, 0, "0xFFFFFF", nombre, formatAgenda);

/*				var labelConvencion:TextField = new TextField();
				labelConvencion.type=TextFieldType.DYNAMIC;
				labelConvencion.defaultTextFormat = formatAgenda;
				labelConvencion.selectable = false;
				labelConvencion.mouseEnabled = false;
				labelConvencion.autoSize = TextFieldAutoSize.CENTER;
				labelConvencion.antiAliasType = AntiAliasType.ADVANCED; 
				labelConvencion.border = false;
				labelConvencion.text = nombre;
				labelConvencion.x = posicionX+ancho+5;
				labelConvencion.y = y0+((alto/2)-10);
				labelConvencion.border=true;*/
				

				convenciones.addChild(labelConvencion);
				
				convenciones.addChild(celda);
				posicionX+=ancho+espacioParaNombre;
			}
			var celdaNoDisponible:Sprite = crearCelda(posicionX, y0, ancho, alto, borde, "0xEEEEEE", "", formatAgenda);
			var labelConvencionNoDisponible:Sprite = crearCelda(posicionX+ancho, y0, espacioParaNombre, alto, 0, "0xFFFFFF", "No Disponibles", formatAgenda);
			convenciones.addChild(celdaNoDisponible);
			convenciones.addChild(labelConvencionNoDisponible);

			scrollConvenciones.source=convenciones;
		}
	
		/**
		 * Función que verifica si un elemento se encuentra en una lista XML
		 * Retorna el elementoXML donde se asignó el cupo
		 */
		private function contiene(texto:Object, xmlGeneradas:XMLList):XML
		{
			for each(var generada:XML in xmlGeneradas)
			{
				for each(var cupo:XML in generada.cupos.elements())
				{
					if(cupo.hora==texto)
					{
						var col:String=generada.color;
						col="0x"+col.substr(1, col.length);
						cupo.color=col;
						cupo.codigo_pk=generada.codigo_pk;
						cupo.@permiteAsignar=generada.@permiteAsignar;
						cupo.@permiteReservar=generada.@permiteReservar;
						cupo.@permiteCupoExtra=generada.@permiteCupoExtra;
						return cupo;
					}
				}
			}
			return null;
		}
		
		private function adicionarCitas(texto:Object, xmlGeneradas:XMLList):XMLList
		{
			var citas:XMLList = null;
			for each(var generada:XML in xmlGeneradas)
			{
				for each(var cita:XML in generada.citas.elements())
				{
					if(cita.hora_inicio<=texto && cita.hora_fin>texto)
					{
						if(citas==null)
						{
							citas = new XMLList(cita);
						}
						else
						{
							citas = new XMLList(citas.toString()+cita.toString());
						}
					}
				}
			}
			return citas;
		}
		
		private function seleccionar(actividad:String):void
		{
			if(consultorioSeleccionado!=null)
			{
				var espacios:Lista=consultorioSeleccionado.getEspacios();
				var horaInicioS:String = espacios.getHoraInicioSeleccion();
				var horaFinS:String = espacios.getHoraFinSeleccion();
				var codigoAgenda:int = espacios.getCodigoAgendaSeleccionado();
				var cupoExtra:Boolean = espacios.getActual().getAsignado();
				ExternalInterface.call("asignar", horaInicioS, horaFinS, codigoAgenda, actividad, cupoExtra);
			}
			else
			{
				//trace("no hay nada seleccionado");
			}
		}
		
		private function popupActividad(target:EspacioTiempo, stageX:int, stageY:int)
		{
			if(popup!=null)
			{
				this.removeChild(popup);
				popup=null;
			}

			var espacioActual:EspacioTiempo=EspacioTiempo(consultorioSeleccionado.getEspacios().getActual());
			var asignado:Boolean=espacioActual.getAsignado();
			if(actividad!=null && !asignado)
			{
				seleccionar(actividad);
				return;
			}

			var permiteAsignar:Boolean=espacioActual.getPermiteAsignar();
			var permiteReservar:Boolean=espacioActual.getPermiteReservar();
			var permiteCupoExtra:Boolean=espacioActual.getPermiteCupoExtra();
			var posX:int=stageX-2;
			var posY:int=stageY-2;
			var altoInfo:int=90;
			var anchoInfo:int=200;
			if(posX+anchoInfo>anchoEscenario)
			{
				posX=stageX-anchoInfo+1;
			}
			if(posY+altoInfo>largoEscenario)
			{
				posY=stageY-altoInfo+1;
			}
			popup=crearMenuOpciones(posX, posY, anchoInfo, altoInfo, 6, '0xFFFFFF', formatMensajes);
			this.removeChild(info);
			info=null;
			this.addChild(popup);
			if(!permiteCupoExtra)
			{
				agregarActividades();
			}

			if(asignado)
			{
				if(permiteCupoExtra)
				{
					var botonCupoExtra:Button=new Button();
					botonCupoExtra.width=180;
					botonCupoExtra.height=30;
					botonCupoExtra.x=10;
					botonCupoExtra.y=32;
					botonCupoExtra.addEventListener(MouseEvent.MOUSE_DOWN, actividadCupoExtra);
					botonCupoExtra.label="Cupo Extra";
					popup.addChild(botonCupoExtra);
				}
				else
				{
					var formatInfo:TextFormat = new TextFormat();
					formatInfo.color = 0x000000;
					formatInfo.size = 11;
					formatInfo.bold = true;
					formatInfo.font = "Arial";
					formatInfo.italic = false;
					var labelError:String="CUPO NO DISPONIBLE";
					adicionarLabelEspacioTiempoCoordenadas(popup, labelError, formatInfo, 40, 10);
					formatInfo.bold=false;
					labelError="No es posible Generar un Cupo Extra.";
					adicionarLabelEspacioTiempoCoordenadas(popup, labelError, formatInfo, 10, 40);
					labelError="Por favor revisar.";
					adicionarLabelEspacioTiempoCoordenadas(popup, labelError, formatInfo, 52, 60);
				}
			}
		}
		
		private function actividadAsignar(event:MouseEvent):void
		{
			seleccionar(Constantes.actividadAsignar);
		}

		private function actividadReservar(event:MouseEvent):void
		{
			seleccionar(Constantes.actividadReservar);
		}
		
		private function mostrarActividadesCupoExtra(event:MouseEvent):void
		{
			var posX:int=popup.x;
			var posY:int=popup.y;
			var altoInfo:int=90;
			var anchoInfo:int=200;
			
			if(posX+anchoInfo>anchoEscenario)
			{
				posX=posX-anchoInfo+1;
			}
			if(posY+altoInfo>largoEscenario)
			{
				posY=posY-altoInfo+1;
			}

			if(popup!=null)
			{
				this.removeChild(popup);
				popup=null;
			}
			if(actividad!=null)
			{
				seleccionar(actividad);
				return;
			}
			popup=crearMenuOpciones(posX, posY, anchoInfo, altoInfo, 6, '0xFFFFFF', formatMensajes);
			this.addChild(popup);
			agregarActividades();
		}
		
		private function actividadCupoExtra(event:MouseEvent):void
		{
			var posX:int=popup.x;
			var posY:int=popup.y;
			var altoInfo:int=90;
			var anchoInfo:int=370;
//			var anchoInfoAnterior:int=popup.width;
			var anchoInfoAnterior:int=200;

			if(posX+anchoInfo>anchoEscenario)
			{
				posX=posX-(anchoInfo-anchoInfoAnterior);
			}
			if(posY+altoInfo>largoEscenario)
			{
				posY=posY-altoInfo+1;
			}

			if(popup!=null)
			{
				this.removeChild(popup);
				popup=null;
			}
			popup=crearMenuOpciones(posX, posY, anchoInfo, altoInfo, 6, '0xFFFFFF', formatMensajes);
			
			this.addChild(popup);

			var formatInfo:TextFormat = new TextFormat();
			formatInfo.color = 0x000000;
			formatInfo.size = 11;
			formatInfo.bold = true;
			formatInfo.font = "Arial";
			formatInfo.italic = false;
			var labelPregunta:String="CUPO EXTRA";
			adicionarLabelEspacioTiempoCoordenadas(popup, labelPregunta, formatInfo, 140, 10);
			formatInfo.bold=false;
			labelPregunta="Esta seguro de generar un Cupo adicional para el cupo seleccionado?:";
			adicionarLabelEspacioTiempoCoordenadas(popup, labelPregunta, formatInfo, 10, 30);


			var botonSi:Button=new Button();
			botonSi.width=30;
			botonSi.height=30;
			botonSi.x=130;
			botonSi.y=50;
			botonSi.addEventListener(MouseEvent.MOUSE_DOWN, mostrarActividadesCupoExtra);
			botonSi.label="SI";
			botonSi.name=BOTON_SI;
			popup.addChild(botonSi);

			var botonNo:Button=new Button();
			botonNo.width=30;
			botonNo.height=30;
			botonNo.x=190;
			botonNo.y=50;
			botonNo.addEventListener(MouseEvent.MOUSE_DOWN, cerrarPopup);
			botonNo.label="No";
			botonNo.name=BOTON_NO;
			popup.addChild(botonNo);

			/*
			this.removeChild(popup);
			popup=null;
			seleccionar(Constantes.actividadCupoExtra);*/
		}
		
		private function agregarActividades():void
		{
			var espacioActual:EspacioTiempo=EspacioTiempo(consultorioSeleccionado.getEspacios().getActual());
			var permiteAsignar:Boolean=espacioActual.getPermiteAsignar();
			var permiteReservar:Boolean=espacioActual.getPermiteReservar();
			if(permiteAsignar)
			{
				var botonAsignar:Button=new Button();
				botonAsignar.width=180;
				botonAsignar.height=30;
				botonAsignar.x=10;
				botonAsignar.y=10;
				botonAsignar.label="Asignar";
				botonAsignar.addEventListener(MouseEvent.MOUSE_DOWN, actividadAsignar);
				popup.addChild(botonAsignar);
			}
			if(permiteReservar)
			{
				var botonReservar:Button=new Button();
				botonReservar.width=180;
				botonReservar.height=30;
				botonReservar.x=10;
				botonReservar.y=50;
				botonReservar.addEventListener(MouseEvent.MOUSE_DOWN, actividadReservar);
				botonReservar.label="Reservar";
				popup.addChild(botonReservar);
			}

		}

		private function cerrarPopup(event:MouseEvent):void
		{
			this.removeChild(popup);
			popup=null;
		}

		private function cerrarPopupAsignacionReserva(event:MouseEvent):void
		{
			if(event.target is Sprite && !(event.target is Button))
			{
				var target:Sprite=Sprite(event.target);
				if(
				   		!((event.stageX>target.x && event.stageX<target.x+target.width-1)
						&&
				   		(event.stageY>target.y && event.stageY<target.y+target.height-1))
				)
				{
					this.removeChild(popup);
					popup=null;
				}
			}
		}

		// Eventos
		private function scrollEvent(event:ScrollEvent):void {
			var position : Number = event.position;
			if( event.direction == ScrollBarDirection.VERTICAL )
			{
				organizarScroll();
				scrollHoras.verticalScrollPosition = position;
			}
			if( event.direction == ScrollBarDirection.HORIZONTAL )
			{
				scrollConsultorios.horizontalScrollPosition = position;
			}
		}
		
		private function mouseDown(event:MouseEvent):void {
			if(event.target is EspacioTiempo)
			{
				if(consultorioSeleccionado!=null)
				{
					var espacios:Lista=consultorioSeleccionado.getEspacios();
					espacios.deseleccionar(Lista.TODOS)
					consultorioSeleccionado=null;
				}
				var target:EspacioTiempo=EspacioTiempo(event.target);
				if(target.getHabilitado())
				{
					if(!target.getCupoExtra())
					{
						mousePresionado=true;
					}
					consultorioSeleccionado=target.getConsultorio();
					consultorioSeleccionado.getEspacios().ubicarEn(target);
					target.seleccionar();
				}
			}
			else
			{
				if(consultorioSeleccionado!=null)
				{
					if(event.target is Button)
					{
						var botonPresionado:Button=Button(event.target);
						if(botonPresionado.name==BOTON_NO)
						{
							consultorioSeleccionado.getEspacios().deseleccionar(Lista.TODOS);
							consultorioSeleccionado=null;
						}
					}
					else if(event.target is Sprite)
					{
						var spritePresionado:Sprite=Sprite(event.target);
						if(spritePresionado.name!=DIALOGO)
						{
							consultorioSeleccionado.getEspacios().deseleccionar(Lista.TODOS);
						}
					}
				}
			}
		}

		private function mouseMove(event:MouseEvent):void {
			if(mousePresionado)
			{
				var posY:int=scrollPane.verticalScrollPosition+event.stageY;
				consultorioSeleccionado.getEspacios().seleccionarHastaCoordenada(posY);
			}
		}

		private function mouseUp(event:MouseEvent):void {
			mousePresionado=false;
			if(event.target is EspacioTiempo)
			{
				var target:EspacioTiempo=EspacioTiempo(event.target);
				if(consultorioSeleccionado!=null && target.getHabilitado())
				{
					popupActividad(target, event.stageX, event.stageY);
				}
			}
		}

		private function mouseOver(event:MouseEvent):void {
			if(event.target is EspacioTiempo)
			{
				var target:EspacioTiempo=EspacioTiempo(event.target);
				generarInfo(target, event.stageX, event.stageY);
			}
		}
		private function mouseOut(event:MouseEvent):void {
			if(info!=null)
			{
				this.removeChild(info);
				info=null;
			}
		}
		private function keyDown(event:KeyboardEvent):void
		{
			var scrollSize:int = 60; 
			organizarScroll();
			if(event.keyCode==Keyboard.DOWN)
			{
				scrollConsultorios.verticalScrollPosition+=scrollSize;
				scrollPane.verticalScrollPosition+=scrollSize;;
			}
			if(event.keyCode==Keyboard.UP)
			{
				scrollConsultorios.verticalScrollPosition-=scrollSize;
				scrollPane.verticalScrollPosition-=scrollSize;;
			}
			if(event.keyCode==Keyboard.RIGHT)
			{
				scrollConsultorios.horizontalScrollPosition+=scrollSize;
				scrollPane.horizontalScrollPosition+=scrollSize;;
			}
			if(event.keyCode==Keyboard.LEFT)
			{
				scrollConsultorios.horizontalScrollPosition-=scrollSize;
				scrollPane.horizontalScrollPosition-=scrollSize;;
			}
			if(event.keyCode==Keyboard.ENTER || event.keyCode==Keyboard.SPACE)
			{
				/*
				var target:EspacioTiempo=EspacioTiempo(event.target);
				popupActividad(target, event.stageX, event.stageY);
				*/
			}
			organizarScroll();
			scrollHoras.verticalScrollPosition=scrollPane.verticalScrollPosition;
			scrollConsultorios.horizontalScrollPosition=scrollPane.horizontalScrollPosition;

		}
		private function mouseWheel(event:MouseEvent):void
		{
			organizarScroll();
			scrollHoras.verticalScrollPosition=scrollPane.verticalScrollPosition;
		}
		
		/*
		 * Sincroniza el área de desplazamiento del scrollPane y del scrolHoras
		 */
		private function organizarScroll():void
		{
			if(scrollPane.verticalPageScrollSize!=scrollHoras.height)
			{
				scrollHoras.height=scrollPane.verticalPageScrollSize;
			}
		}

		private function generarInfo(target:EspacioTiempo, stageX:int, stageY:int):void
		{
			/* crear la celda */
			var celda:Sprite = new Sprite();
			celda.x=x;
			celda.y=y;
			
			var cant:int = ("Profesional: "+target.getProfesional()).length;
			
			if(cant<("Especialidad: "+target.getEspecialidad()).length)
			{
				cant=("Especialidad: "+target.getEspecialidad()).length;
			}
			
			var textoInfo:String;
			
			var posInfo:Lista = new Lista();
	
			if(target.getCita()!=null)
			{
				var renglon:int=16;
				altoInfo=renglon;
				var citas:XMLList = target.getCita();
				textoInfo="Profesional: "+target.getProfesional()+" - "+target.getEspecialidad()+"\n";
				if(cant<textoInfo.length)
				{
					cant=textoInfo.length;
				}
	
				for each(var cita:XML in citas)
				{
					var cadTempo:String = "*** "+cita.estado+"  |  "+cita.tipo +"  |  "+cita.duracion+" Minutos"+"  |  "+cita.plan;
					if(cant<cadTempo.length)
					{
						cant=cadTempo.length;
					}
					textoInfo+=cadTempo+"\n";;
					cadTempo="      "+cita.hora_inicio+" - "+cita.hora_fin;
					if(cant<cadTempo.length)
					{
						cant=cadTempo.length;
					}
					textoInfo+=cadTempo+"\n";;
					cadTempo="      "+cita.paciente.nombre+" ";
					if(cant<cadTempo.length)
					{
						cant=cadTempo.length;
					}
					textoInfo+=cadTempo+"\n";
					
					var renglones:int =3;
					
					for each(var servicio:XML in cita.servicios.elements())
					{
						cadTempo="      SERVICIO:";
						if(servicio.nombre.length>35)
						{
							cadTempo+=servicio.nombre.substr(0, 35);
						}
						else
						{
							cadTempo+=servicio.nombre;
							var tempoNombre:String = servicio.nombre;
							for(var j:int=0; j<(35-tempoNombre.length); j++)
							{
								cadTempo+=" ";
							}
						}
						if(cant<cadTempo.length)
						{
							cant=cadTempo.length;
						}
						var cadenaTempo2:String="";
						if(servicio.condiciones!=null && servicio.condiciones.elements().length()>0)
						{
							cadenaTempo2+="\n      * Condiciones:\n        ";
							renglones++;
							for each(var condicion:XML in servicio.condiciones.elements())
							{
								cadenaTempo2+=condicion;
								cadenaTempo2+="  |  "
							}
						}
						if(cant<cadenaTempo2.length)
						{
							cant=cadenaTempo2.length;
						}
						cadTempo+=cadenaTempo2+"\n";
						textoInfo+=cadTempo;
						renglones++;
					}
					
					altoInfo+=renglon*renglones;
					//posInfo.add(altoInfo);
				}
			}
			else
			{
				textoInfo=
						"Hora inicio: "+target.getHora()+"\n"+
						"Hora fin: "+target.getHoraFin()+"\n"+
						"Profesional: "+target.getProfesional()+"\n"+
						"Especialidad: "+target.getEspecialidad()+"\n";
						altoInfo=65;
			}
			altoInfo+=6;
			anchoInfo=(cant*6.1)+8;
			var posX:int=stageX+10;
			var posY:int=stageY+10
			if(stageX+10+anchoInfo>anchoEscenario)
			{
				posX=stageX-anchoInfo+1;
			}
			if(stageY+10+altoInfo>largoEscenario)
			{
				posY=stageY-altoInfo+1;
			}
			if(posX<0)
			{
				posX=10;
			}
			
			celda.x=posX;
			celda.y=posY;
			celda.graphics.beginFill(uint("0xFFFFFF"));
			celda.graphics.lineStyle(1,uint("0x000000"),0.1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			celda.graphics.drawRoundRect(0, 0, anchoInfo, altoInfo, 1, 1);
			celda.graphics.endFill();
	
			// Se dibujan las líneas de separación
			for(var i:int; i<posInfo.size()-1; i++)
			{
				celda.graphics.lineStyle(1, 0x000000, 0.2, false, LineScaleMode.VERTICAL, CapsStyle.NONE, JointStyle.MITER);
				celda.graphics.moveTo(3, uint(posInfo.get(i)))
				celda.graphics.lineTo(anchoInfo-3, uint(posInfo.get(i)));
			}
	
			
			// se adiciona el texto
			var formatInfo:TextFormat = new TextFormat();
			formatInfo.color = 0x000000;
			formatInfo.size = 11;
			formatInfo.bold = false;
			formatInfo.font = "Arial";
			formatInfo.italic = false;
	
			var labelInfo:TextField = new TextField();
			labelInfo.defaultTextFormat = formatInfo;
			labelInfo.selectable = false;
			labelInfo.mouseEnabled = false;
			labelInfo.autoSize = TextFieldAutoSize.CENTER;
			labelInfo.antiAliasType = AntiAliasType.NORMAL; 
			labelInfo.border = false;
			labelInfo.text = textoInfo;
			labelInfo.x = 7;
			labelInfo.y = 3;
	
			celda.addChild(labelInfo);
			
			info=celda;
	
			this.addChild(celda);
		}
	}
}