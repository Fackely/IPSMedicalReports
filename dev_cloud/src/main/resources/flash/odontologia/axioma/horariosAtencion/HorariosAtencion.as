package axioma.horariosAtencion {
	
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
	
	import flash.ui.Keyboard;
	
	import axioma.estructuras.Lista;
	import axioma.utilidades.UtilidadesMovieClip;
	import axioma.comun.EspacioTiempo;
	import axioma.comun.Consultorio;

	
	public class HorariosAtencion extends MovieClip{
		
		private var xmlHorariosCompleto:String = "" ;
		private var xmlConsultorios:String = "" ;
		private var xmlParametros:String = "" ;	
		
		private var formatMensajes:TextFormat;
		
		private var anchoEscenario:int = 0;
		private var largoEscenario:int = 0;
		private var espacioEntreElementos:int = 2;
		
		//private var paramAgenda:ParametrosAgenda;
		
		private var fondo:Sprite;
		private var horarios:Sprite;
		private var consultorios:Sprite;
		private var convenciones:Sprite;
		private var profesionales:Sprite;

		private var colorHorario:String = "0x00F0F0";

		private var anchoInfo:int = 0; //Este se calcula
		private var altoInfo:int = 0;
		private var bordeInfo:int = 5;
		private var info:Sprite;

		private var mousePresionado:Boolean = false;

		private var consultorioSeleccionado:Consultorio;
		
		private var scrollSize:int = 23;
		
		var error:Sprite = null;
		
		private var tipoDia:Boolean=false;

		// Initialization:
		public function HorariosAtencion(){
			// este xml es de prueba
			var horariosXML:String = 
			'<?xml version="1.0" encoding="UTF-8" ?>'+
			'<contenido tipo="consultorio" horaInicio="08:00" horaFin="17:00" intervalo="10" dia="LUNES">'+
			'	<convenciones>'+
			'		<profesionales>'+
			'			<profesional codigo="1" nombre="Juan David Ramírez"/>'+
			'			<profesional codigo="2" nombre="Karen María Suares"/>'+
			'			<profesional codigo="3" nombre="Eustaquio Alejandro Cardona"/>'+
			'		</profesionales>'+
			'		<unidadesAgenda>'+
			'			<unidad nombre="REHABILITACIÓN ORAL NO SE QUE TAN LARGO ES" color="#F12546"/>'+
			'			<unidad nombre="Periodoncia" color="#F6A87C"/>'+
			'			<unidad nombre="UNIDAD DE PRUEBA" color="#34FAB5"/>'+
			'		</unidadesAgenda>'+
			'	</convenciones>'+
			'	<horariosAtencion>'+
			'		<horarioAtencion codigoHorario="11668" horaInicio="08:00" horaFin="12:40" profesional="1" colorUnidadAgenda="#F12546" consultorio="0" nombreConsultorio="Cualquiera" diaSemana="LUNES"/>'+
			'		<horarioAtencion codigoHorario="11669" horaInicio="12:00" horaFin="17:00" profesional="2" colorUnidadAgenda="#F6A87C" consultorio="1" nombreConsultorio="OTRO DE 15 ELEMENTOS" diaSemana="MARTES"/>'+
			'		<horarioAtencion codigoHorario="11670" horaInicio="09:20" horaFin="11:40" profesional="3" colorUnidadAgenda="#34FAB5" consultorio="1" nombreConsultorio="OTRO DE 15 ELEMENTOS" diaSemana="MARTES"/>'+
			'	</horariosAtencion>'+
			'</contenido>';
			
			var prueba:String = 
			'<?xml version="1.0" encoding="UTF-8" standalone="yes"?><contenido dia="" intervalo="10" tipo="consulrotio"><convenciones/><horariosAtencion/></contenido>';
//			asignarXML(horariosXML);
//			asignarXML(prueba);
//			asignarXML("");
			ExternalInterface.addCallback("asignarXML",asignarXML);
		}
		
		public function asignarXML(creacionHorariosStr:String):int
		{
			var creacionAgendaXML:XML = new XML(creacionHorariosStr);
			var convencionesXML:XML = creacionAgendaXML.convenciones[0];
			
			var incrementoLabelDia:int=0;
			if(creacionAgendaXML.@tipo=="dia")
			{
				this.tipoDia=true;
				incrementoLabelDia=25+2;
			}
			else
			{
				this.tipoDia=false;
				incrementoLabelDia=0;
			}
			
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
			scrollProfesionales.source=null;

			if(creacionHorariosStr=="" || convencionesXML==""  )
			{
				var formatError = new TextFormat();
				formatError.color = 0x000000;
				formatError.size = 15;
				formatError.bold = true;
				formatError.italic = false;
				formatError.font = "Arial";

				error = UtilidadesMovieClip.crearCelda(100, 50, 600, 30, 10, '0x3299FF', "No hay información con los parámetros de búsqueda seleccionados", formatError, 20, 3);
				this.addChild(error);
				return -1;
			}
			
			
			anchoEscenario = 700;
			largoEscenario = 600;

			fondo = new Sprite();
			horarios = new Sprite();
			consultorios = new Sprite();
			convenciones = new Sprite();
			profesionales = new Sprite();

			// Tamaños
			scrollPane.width=770;
			scrollPane.height=443-incrementoLabelDia; // Se disminuye cuando tiene label dia
			
			scrollPane.x=70;
			scrollPane.y=157+incrementoLabelDia;
			
			scrollConsultorios.width=755;
			scrollConsultorios.height=25;
			
			scrollConsultorios.x=70;
			scrollConsultorios.y=130+incrementoLabelDia;
			
			scrollHoras.width=70;
			scrollHoras.height=428-incrementoLabelDia; // Se disminuye cuando tiene label dia

			scrollHoras.x=0;
			scrollHoras.y=157+incrementoLabelDia;

			scrollConvenciones.width=420;
			scrollConvenciones.height=100;

			scrollConvenciones.x=420;
			scrollConvenciones.y=30;

			scrollProfesionales.width=420;
			scrollProfesionales.height=100;

			scrollProfesionales.x=0;
			scrollProfesionales.y=30;

			inicializarConvenciones(XML(convencionesXML));
			
			inicializarHorarios(creacionAgendaXML, incrementoLabelDia);
			
			scrollPane.source=fondo;
			scrollPane.addEventListener(ScrollEvent.SCROLL, scrollEvent);

			scrollPane.verticalLineScrollSize=scrollSize;

			scrollHoras.source=horarios;
			
			scrollConsultorios.source=consultorios;
			
			scrollHoras.verticalScrollPolicy=ScrollPolicy.OFF;
			scrollHoras.horizontalScrollPolicy=ScrollPolicy.OFF;
			scrollConsultorios.verticalScrollPolicy=ScrollPolicy.OFF;
			scrollConsultorios.horizontalScrollPolicy=ScrollPolicy.OFF;
			
			if(scrollPane.verticalPageScrollSize>scrollHoras.height)
			{
				scrollHoras.height=scrollPane.verticalPageScrollSize;
			}
			
			formatMensajes = new TextFormat();
			formatMensajes.color = 0x000000;
            formatMensajes.size = 11;
            formatMensajes.bold = true;
            formatMensajes.italic = false;
			formatMensajes.font = "Arial";
			
/*			this.addEventListener(MouseEvent.MOUSE_DOWN, mouseDown);
			this.addEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
			this.addEventListener(MouseEvent.MOUSE_UP, mouseUp);*/
			this.addEventListener(MouseEvent.MOUSE_WHEEL, mouseWheel);
			
			this.addEventListener(KeyboardEvent.KEY_DOWN, keyDown);
			
			return 0;
		}
		
		
		private function inicializarConvenciones(xmlConvenciones:XML):void
		{
			var xmlProfesionales:XML = xmlConvenciones.profesionales[0];
			var xmlUnidadesAgenda:XML = xmlConvenciones.unidadesAgenda[0];
			
			// definición de la letra
			var formatAgenda:TextFormat = new TextFormat();
			formatAgenda.color = 0x000000;
			formatAgenda.size = 15;
			formatAgenda.bold = true;
			formatAgenda.font = "Arial";
			formatAgenda.italic = false;
			
			var x0:int = 40;
			var posicionX:int = x0;
			var y0:int = 0;
			var posicionY:int = y0;
			var ancho:int = 20;
			var alto:int = 20;
			var borde:int = 10;
			var maxLetras:int =25;
			var espacioParaNombre:int =maxLetras*6.6;
			var espacioCeldas:int = 2;


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

			var tamanioLetra=10;

			formatAgenda.color = 0x000000;
			formatAgenda.size = tamanioLetra;
			formatAgenda.bold = true;
			formatAgenda.font = "Arial";
			formatAgenda.italic = false;

			var i:int=0;
			var xLabel:int = 7;
			var yLabel:int = 1;

			for each(var unidadAgenda:XML in xmlUnidadesAgenda.elements())
			{
				var col:String = unidadAgenda.@color;
				col = "0x"+col.substr(1, col.length);
				var celda:Sprite = UtilidadesMovieClip.crearCelda(posicionX, posicionY, ancho, alto, borde, col, "", formatAgenda, xLabel, yLabel);
				
				var nombre:String = unidadAgenda.@nombre;
				if(nombre.length>15)
				{
					nombre=nombre.substring(0, maxLetras);
				}

				var labelConvencion:Sprite = UtilidadesMovieClip.crearCelda(posicionX+ancho, posicionY, espacioParaNombre, alto, 0, "0xFFFFFF", nombre, formatAgenda, xLabel, yLabel);

				convenciones.addChild(labelConvencion);
				
				convenciones.addChild(celda);
				posicionX+=ancho+espacioParaNombre;
				i++;
				if(i==2)
				{
					posicionX=x0;
					posicionY+=alto+espacioCeldas;
					i=0;
				}
			}
			scrollConvenciones.source=convenciones;

			// Genero los profesionales
			i=0;
			posicionX=x0;
			posicionY=y0;
			xLabel = 7;
			yLabel = 1;

			for each(var profesional:XML in xmlProfesionales.elements())
			{
				var codigo:String = profesional.@codigo;
				var nombreProf:String = profesional.@nombre;
				if(nombreProf.length>15)
				{
					nombreProf=nombreProf.substring(0, maxLetras);
				}
				var celdaProf:Sprite = UtilidadesMovieClip.crearCelda(posicionX, posicionY, espacioParaNombre, alto, borde, '0xFBFCB8' ,codigo+": "+nombreProf, formatAgenda, xLabel, yLabel);
				
				profesionales.addChild(celdaProf);
				posicionX+=ancho+espacioParaNombre;
				i++;
				if(i==2)
				{
					posicionX=x0;
					posicionY+=alto+espacioCeldas;
					i=0;
				}
			}
			
			scrollProfesionales.source=profesionales;
		}

		private function inicializarHorarios(horariosXML:XML, incrementoLabelDia:int):void
		{
			var horariosAtencionXML:XML = horariosXML.horariosAtencion[0];

			// Formato de la letra
			var formatAgenda:TextFormat = new TextFormat();
			formatAgenda.color = 0x000000;
			formatAgenda.font = "Arial";
			
			var y0:int = 130+incrementoLabelDia;
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
			var xLabel:int = 7;
			var yLabel:int = 3;
			var tamanioLetra:int=10;
			

			intervalo=horariosXML.@intervalo;
			horaInicio=horariosXML.@horaInicio;
			minutoInicio=horariosXML.@minInicio;
			horaFin=horariosXML.@horaFin;
			minutoFin=horariosXML.@minFin;
			
			if(intervalo==null)
			{
				trace("Error en el XML");
				return;
			}

			// Si es tipo día, entonces agrego el label con el día seleccionado
			if(this.tipoDia)
			{
				formatAgenda.size=tamanioLetra+3;
				formatAgenda.bold=true;
				this.addChild(UtilidadesMovieClip.crearCelda(70, 130, 755, 25, borde, colorHorario ,horariosXML.@dia, formatAgenda, 360, 3));
				formatAgenda.bold=false;
				formatAgenda.size=tamanioLetra;
			}
			
			var hora:Date = new Date();
			var horaLimite:Date = new Date();
			hora.setHours(Number(horaInicio.substr(0,2)), Number(horaInicio.substr(3,4)), Number(0));
			
			horaLimite.setHours(Number(horaFin.substr(0,2)), Number(horaFin.substr(3,4)), Number(0));
			
			formatAgenda.size = 12;
			formatAgenda.bold = true;
			var celda:Sprite = UtilidadesMovieClip.crearCelda(posicionX, y0, ancho, alto, borde, colorHorario, "HORA", formatAgenda, xLabel, yLabel);
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
				textoHora=String(hor<10?"0"+hor:hor);
				textoHora+=":";
				textoHora+=min<10?"0"+min:min;

				var textoHoraFin:String = "";
				
				var horaFinal:Date = new Date();
				horaFinal.setTime(hora.getTime()+(Number(intervalo)*60*1000)-1);
				
				hor = horaFinal.getHours();
				min = horaFinal.getMinutes();
				textoHoraFin=String(hor<10?"0"+hor:hor);
				textoHoraFin+=":";
				textoHoraFin+=min<10?"0"+min:min;

				listaHorarios.add(textoHora);
				listaHorariosFin.add(textoHoraFin);
				
				celda = UtilidadesMovieClip.crearCelda(posicionX, posicionY, ancho, alto, borde, colorHorario, textoHora, formatAgenda, xLabel, yLabel);
				
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
			var consultoriosGenerados:Lista = new Lista();

			for each(var horario:XML in horariosAtencionXML.elements())
			{
				posicionY=0;
				
				var horaInicioCelda:Date = new Date();
				var horaFinCelda:Date = new Date();
				horaInicioCelda.setHours(Number(horario.@horaInicio.substr(0,2)), Number(horario.@horaInicio.substr(3,2)), Number(0));
				horaFinCelda.setHours(Number(horario.@horaFin.substr(0,2)), Number(horario.@horaFin.substr(3,2)), Number(0));
					
				hor = horaInicioCelda.getHours();
				min = horaInicioCelda.getMinutes();
				textoHora=String(hor<10?"0"+hor:hor);
				textoHora+=":";
				textoHora+=min<10?"0"+min:min;

				hor = horaFinCelda.getHours();
				min = horaFinCelda.getMinutes();
				textoHoraFin=String(hor<10?"0"+hor:hor);
				textoHoraFin+=":";
				textoHoraFin+=min<10?"0"+min:min;

				formatAgenda.size = 11;
				formatAgenda.bold = false;
				var encabezadoColumnaStr:String;

				var consultorio:Consultorio=null;

				var incrementar:Boolean=false;
				for(var k:int=0; k<consultoriosGenerados.size(); k++)
				{
					var consultorioInterno:Consultorio=Consultorio(consultoriosGenerados.get(k));
					if(horario.@consultorio==consultorioInterno.getCodigo())
					{
						consultorio=consultorioInterno;
						break;
					}
				}
				if(consultorio==null)
				{
					consultorio=new Consultorio();
					consultorio.setCodigo(horario.@consultorio);
					incrementar=true;
				}
				if(incrementar && consultoriosGenerados.size()>0)
				{
					posXCita+=anchoConsultorio+espacioCeldas;
					posXCitaEnScrollPane+=anchoConsultorio+espacioCeldas;
				}

				if(tipoDia)
				{
					encabezadoColumnaStr = horario.@nombreConsultorio;
				}
				else
				{
					encabezadoColumnaStr = horario.@diaSemana;
				}
				if(encabezadoColumnaStr.length > 15)
				{
					encabezadoColumnaStr=encabezadoColumnaStr.substr(0, 15);
				}
				celda = UtilidadesMovieClip.crearCelda(posXCita, posicionY, anchoConsultorio, alto, borde, colorHorario, encabezadoColumnaStr, formatAgenda, xLabel, yLabel);
				consultorios.addChild(celda);
				
				
				
				for(var j=0; j<numeroHorarios; j++)
				{
					var espacio:EspacioTiempo=null;

					if(String(listaHorarios.get(j))>=textoHora && String(listaHorarios.get(j))<textoHoraFin)
					{
						var col:String = horario.@colorUnidadAgenda;
						col = '0x'+col.substr(1, col.length-1);
/*
						trace(col);
						var red:String = col.substr(0,2);
						trace(red);
						var green:String = col.substr(2,2);
						trace(green);
						var blue:String = col.substr(4,2);
						trace(blue);
						
						trace('0x'+red);
						trace(uint('0x'+red));
						trace(uint('0x99'));

						if(uint('0x'+red)>uint('0x99'))
						{
							red=(uint('0xAA')-uint('0x99')).toString(16);
							trace(red);
						}
						trace(green);
						col="0x"+red+green+blue;
*/
						formatAgenda.bold=true;
						if(incrementar)
						{
							espacio = EspacioTiempo(crearEspacioTiempo(posXCitaEnScrollPane, posicionY, anchoConsultorio, alto, borde, col, horario.@profesional, formatAgenda, xLabel, yLabel));
						}
						else
						{
							espacio = EspacioTiempo(consultorio.getEspacios().get(j));
							espacio = EspacioTiempo(crearEspacioTiempo(posXCitaEnScrollPane, posicionY, anchoConsultorio, alto, borde, col, horario.@profesional, formatAgenda, xLabel, yLabel));
							fondo.addChild(espacio);
						}
						/*
						espacio.addEventListener(MouseEvent.MOUSE_OVER, mouseOver);
						espacio.addEventListener(MouseEvent.MOUSE_OUT, mouseOut);
						*/
						espacio.setHabilitado(false);
						if(incrementar)
						{
							consultorio.addEspacio(espacio);
							espacio.setHora(String(listaHorarios.get(j)));
							espacio.setHoraFin(String(listaHorariosFin.get(j)));
							espacio.setConsultorio(consultorio);
							fondo.addChild(espacio);
						}
						posicionY+=alto+espacioCeldas;
					}
					else
					{
						//trace(incrementar);
						if(incrementar)
						{
							espacio = EspacioTiempo(crearEspacioTiempo(posXCitaEnScrollPane, posicionY, anchoConsultorio, alto, borde, "0xEEEEEE", "", formatAgenda, xLabel, yLabel));
							consultorio.addEspacio(espacio);
							espacio.setHora(String(listaHorarios.get(j)));
							espacio.setHoraFin(String(listaHorariosFin.get(j)));
							espacio.setConsultorio(consultorio);
							fondo.addChild(espacio);
						}
						posicionY+=alto+espacioCeldas;
					}
				}
				if(incrementar)
				{
					consultoriosGenerados.add(consultorio);
					numeroConsultorio++;
				}
			}
		}
		
		private function crearEspacioTiempo(x:int, y:int, ancho:int, alto:int, borde:int, color:String, texto:String, formatAgenda:TextFormat, xLabel:int, yLabel:int):Sprite
		{
			var espacioTiempo:EspacioTiempo = new EspacioTiempo();
			
			UtilidadesMovieClip.asignarPropiedadesSprite(espacioTiempo, x, y, ancho, alto, borde, color, texto, formatAgenda, xLabel, yLabel);
			return espacioTiempo;
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

		private function seleccionar():void
		{
			if(consultorioSeleccionado!=null)
			{
				var espacios:Lista=consultorioSeleccionado.getEspacios();
				var horaInicioS:String = espacios.getHoraInicioSeleccion();
				var horaFinS:String = espacios.getHoraFinSeleccion();
				var codigoAgenda:int = espacios.getCodigoAgendaSeleccionado();
				ExternalInterface.call("asignar", horaInicioS, horaFinS, codigoAgenda);
			}
			else
			{
				trace("no hay nada seleccionado");
			}
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
						return cupo;
					}
				}
			}
			return null;
		}

		// Eventos
		private function scrollEvent(event:ScrollEvent):void {
			var position : Number = event.position;
			if( event.direction == ScrollBarDirection.VERTICAL )
			{
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
					mousePresionado=true;
					consultorioSeleccionado=target.getConsultorio();
					consultorioSeleccionado.getEspacios().ubicarEn(target);
					target.seleccionar();
				}
			}
			else
			{
				if(consultorioSeleccionado!=null)
				{
					consultorioSeleccionado.getEspacios().deseleccionar(Lista.TODOS);
				}
				consultorioSeleccionado=null;
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
			seleccionar();
		}

		private function mouseOver(event:MouseEvent):void {
			if(event.target is EspacioTiempo)
			{
				var target:EspacioTiempo=EspacioTiempo(event.target);
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
				seleccionar();
			}
			scrollHoras.verticalScrollPosition=scrollPane.verticalScrollPosition;
			scrollConsultorios.horizontalScrollPosition=scrollPane.horizontalScrollPosition;

		}
		private function mouseWheel(event:MouseEvent):void
		{
			scrollHoras.verticalScrollPosition=scrollPane.verticalScrollPosition;
		}

	}	
}