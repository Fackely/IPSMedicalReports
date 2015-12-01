package mundo.indicePlaca
{
    import fl.controls.*;
	import flash.display.MovieClip;	 
	import flash.display.BitmapData;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.text.AntiAliasType;
	import flash.external.ExternalInterface;
	import flash.events.MouseEvent;
	import flash.events.*;
	
	import flash.utils.ByteArray;
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;	
	import flash.net.navigateToURL;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.display.Bitmap;
	import flash.display.Sprite;

	
	import util.corel.src.com.adobe.images.JPGEncoder;
	import util.corel.src.com.adobe.UploadPostHelper;
	import util.corel.src.com.adobe.MultipartURLLoader;

	import util.general.Constantes;
	import mundo.indicePlaca.Diente;
	
	public class IndicePlaca extends MovieClip
	{
		var ct_indplaca:Sprite;
		var numSuperDentSelec:int = 0;
		var numSuperDentPrese:int = 208;
		
		var activoDienteAdulto:String = "" ;
		var activoDienteNino:String = "";
		var labelporcentaje:TextField;
		var labelUnidadMedida:TextField;
		var labelinterpretacion:TextField;
		var labellog:TextField;
		var format:TextFormat;
		
		var arrayDientes:Array;
		var arrayRangosXml:XML;
		
		var xmlIndicePlaca:String;
		var rutaImagen:String;
		var indicadorImagen:String;
		var btn_actualizar:Button;
		
		public function IndicePlaca()
		{
			arrayRangosXml = new XML();
			arrayDientes = new Array();
			ct_indplaca = new Sprite();
						
			labelporcentaje =  new TextField();
			labelUnidadMedida =  new TextField();
			labelinterpretacion =  new TextField();
			labellog =  new TextField();
			format = new TextFormat()
			format.color = 0x000000;
            format.size = 15;
            format.bold = true;
            format.italic = false;
			
			labelporcentaje.defaultTextFormat = format;
			labelUnidadMedida.defaultTextFormat = format;
			labelinterpretacion.defaultTextFormat = format;
			labellog.defaultTextFormat = format;
			
			labelporcentaje.x = 150;
            labelporcentaje.y = 320;
            labelporcentaje.autoSize = TextFieldAutoSize.LEFT;
            labelporcentaje.antiAliasType = AntiAliasType.NORMAL;       
						
			labelUnidadMedida.x = 190;
            labelUnidadMedida.y = 320;
            labelUnidadMedida.autoSize = TextFieldAutoSize.LEFT;
            labelUnidadMedida.antiAliasType = AntiAliasType.NORMAL;
			labelUnidadMedida.text = "%";
			
			labelinterpretacion.x = 510;
            labelinterpretacion.y = 320;
            labelinterpretacion.autoSize = TextFieldAutoSize.LEFT;
            labelinterpretacion.antiAliasType = AntiAliasType.NORMAL;
			
			labellog.x = 0;
            labellog.y = 0;
            labellog.autoSize = TextFieldAutoSize.LEFT;
            labellog.antiAliasType = AntiAliasType.NORMAL;
			
			ct_indplaca.addChild(labellog);
			ct_indplaca.addChild(labelUnidadMedida);
			
			//Añade la funcion a la interfaz
			
			ExternalInterface.addCallback("setSwfActivoDientes",setActivoDientes);
			ExternalInterface.addCallback("setSwfRangos",setRangos);
			ExternalInterface.addCallback("setSwfIndicePlacaXML",setXMLIndicePlaca);
			ExternalInterface.addCallback("setSwfPintarIndicePlaca",pintarIndicePlaca);
			ExternalInterface.addCallback("setSwfInfoImagen",setInfoImagen);

			ExternalInterface.addCallback("getIndicePlacaXML",generarXML);
			crearBtnActualizar();
			
			//llamadoInternoPintar(null);
		}
		
		
		function crearBtnActualizar()
		{
			btn_actualizar = new Button();			
			btn_actualizar.label = "ACTUALIZAR";
			btn_actualizar.setStyle("textFormat", new TextFormat(null,10,0x333333,true));
			btn_actualizar.useHandCursor = true;			
			btn_actualizar.addEventListener(MouseEvent.CLICK,generarmapapixelEvento);			
			btn_actualizar.width = 100;
			btn_actualizar.move(570, 346.1);
			this.addChildAt(btn_actualizar,0);
		}
		
		/**
		*/		
		public function llamadoInternoPintar(e:MouseEvent)
		{
			xmlIndicePlaca = "<contenido><diente pieza='53' indicativo='PLA' ><superficie codigo = '' nombre = '' sector = '1' ></superficie><superficie codigo = '' nombre = '' sector = '2' ></superficie><superficie codigo = '' nombre = '' sector = '3' ></superficie><superficie codigo = '' nombre = '' sector = '4' ></superficie></diente><diente pieza='51' indicativo='AUS'></diente><diente pieza='23' indicativo='PLA' ><superficie codigo = '' nombre = '' sector = '1' ></superficie><superficie codigo = '' nombre = '' sector = '2' ></superficie><superficie codigo = '' nombre = '' sector = '3' ></superficie><superficie codigo = '' nombre = '' sector = '4' ></superficie></diente><diente pieza='61' indicativo='AUS'></diente><diente pieza='-1'> <numSuperDentSelec>'8'</numSuperDentSelec><activoDienteAdulto>'S'</activoDienteAdulto><activoDienteNino>'S'</activoDienteNino><porcentaje>'6.67'</porcentaje><interpretacion>'Aceptable'</interpretacion></diente></contenido>";
			
			this.activoDienteAdulto = "S";
			this.activoDienteNino = "S";
			setRangos("");
			pintarIndicePlaca();
		}
		
		/**
		*/		
		public function llamadoInternoGenerarXML(e:MouseEvent)
		{
			generarXML();			
		}
		
		/**
		
		*/
		public function pintarIndicePlaca()
		{
			this.addChild(ct_indplaca);
			
			primerCuadrante();
			segundoCuadrante();
			tercerCuadrante();
			cuartoCuadrante();			
			
			cargarIndicePlacaXML();
			calcularPorcentajePlaca();
		}
		
		/**
		Actualiza la informaciòn para la generaciòn de la imagen generada
		*/
		public function setInfoImagen(ruta:String,indicador:String)
		{
			rutaImagen = ruta;
			indicadorImagen = indicador;
		}
		
		/**
		Actualiza la informacion de los diente activos
		*/
		public function setActivoDientes(actDieAdult:String, actDieNino:String)
		{
			this.activoDienteAdulto = actDieAdult;
			this.activoDienteNino = actDieNino;
		}
		
		/**
		*/
		public function setXMLIndicePlaca(cadena:String)
		{
			xmlIndicePlaca = cadena;
		}
		
		/**
		*/
		public function setRangos(cadena:String)
		{
			if(cadena == "" || cadena == "null" || cadena.length <= 0 || cadena == "undefined")
			{
				cadena =
						"<Rangos>"+
							"<Rango>"+
								"<rangoinicial>0</rangoinicial>"+
								"<rangofinal>12</rangofinal>"+
								"<descripcion>Aceptable</descripcion>"+
							"</Rango>"+
							"<Rango>"+
								"<rangoinicial>13</rangoinicial>"+
								"<rangofinal>23</rangofinal>"+
								"<descripcion>Regular</descripcion>"+
							"</Rango>"+
							"<Rango>"+
								"<rangoinicial>24</rangoinicial>"+
								"<rangofinal>100</rangofinal>"+
								"<descripcion>Deficiente</descripcion>"+
							"</Rango>"+
						"</Rangos>";
			}			
			
			arrayRangosXml = new XML(cadena);
		}
				
		/**
		Pinta un diente en el movie clip deacuerdo a los parametros			
		*/
		public function pintarDiente(
									 numero:int,
									 posx:int,
									 posy:int,
									 tamano:int,
									 cantsepara:int,
									 posxCheck:int,
									 posyCheck:int,
									 posxlabel:int,
									 posylabel:int,
									 activo:String,
									 excluido:String):void
		{
			var diente:Diente = new Diente(numero,activo,excluido);
			diente.x = posx;
			diente.y = posy;
			diente.height = tamano;
			diente.width = tamano;
			
			//CheckBox
			var cb:CheckBox = new CheckBox();			
			cb.label = "";
			cb.x = posx - posxCheck;
			cb.y = posy + posyCheck;
			diente.setAusente = cb;					
			
			//registra la posiciòn del diente en el movie clip
			var myLabel:Label =  new Label();			
			myLabel.text = numero+"";		
			myLabel.autoSize = TextFieldAutoSize.NONE;
			myLabel.move((diente.x - posxlabel),(posy + posylabel));			

			ct_indplaca.addChild(myLabel);
			
			if(activo == Constantes.acronimoSi)			
				ct_indplaca.addChild(cb);
			else
				diente.alpha = 0.5;
												
			ct_indplaca.addChild(diente);

			var arr:Array = new Array(numero,ct_indplaca.getChildIndex(diente));
			arrayDientes.push(arr);
			
			if(excluido == Constantes.acronimoSi || 
				   activo == Constantes.acronimoNo)	
				numSuperDentPrese = numSuperDentPrese - 4 ;
		}
		
		
		/**
		Posiciona los dientes del primer cuadrante
		*/
		public function primerCuadrante()
		{
			var posx:int = 0;
			var posy:int = 50;
			var cantsepara:int = 40;
			var tamano:int = 34;	
			
			var posxCheck:int = 12;
			var posyCheck:int = 15;
			
			var posxLabel:int = 9;
			var posyLabel:int = -35;
			
			//18 17 16 15 14 13 12 11		
			pintarDiente(18,30,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = 30 + cantsepara;
			
			pintarDiente(17,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(16,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(15,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(14,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(13,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(12,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(11,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
											
			//55 54 53 52 51
			
			posy = 125;
			posxLabel= 9;
			posyLabel= -35;
			
			pintarDiente(55,150,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);
			posx = 150 + cantsepara;
			
			pintarDiente(54,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(53,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(52,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(51,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
		}
		
		
		/**
		Posiciona los dientes del segundo cuadrante
		*/
		public function segundoCuadrante()
		{
			var posx:int = 390;
			var posy:int = 50;
			var cantsepara:int = 40;
			var tamano:int = 34;	
			
			var posxCheck:int = 12;
			var posyCheck:int = 15;
			
			var posxLabel:int = 9;
			var posyLabel:int = -35;
			
			//21 22 23 24 25 26 27 28
			pintarDiente(21,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(22,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(23,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(24,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(25,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(26,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(27,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(28,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;			
			
			//61 62 63 64 65
			
			posx = 390;
			posy = 125;			
			posxLabel= 9;
			posyLabel= -35;
			
			pintarDiente(61,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(62,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(63,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(64,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(65,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
		}
		
		/**
		Posiciona los dientes del tercer cuadrante
		*/
		public function tercerCuadrante()
		{			
			var posx:int = 390;
			var posy:int = 280;
			var cantsepara:int = 40;
			var tamano:int = 34;
			
			var posxCheck:int = 12;
			var posyCheck:int = -37;
			
			var posxLabel:int = 6;
			var posyLabel:int = 15;
			
			//31 32 33 34 35 36 37 38			
			pintarDiente(31,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(32,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(33,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(34,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(35,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(36,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(37,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(38,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;	
						
			//71 72 73 74 75 			
			posx = 390;
			posy = 210;		
			
			posxCheck = 12;
			posyCheck = -37;
			
			posxLabel= 6;
			posyLabel= 15;
			
			pintarDiente(71,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(72,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(73,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(74,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(75,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
		}
		
		/**
		Posiciona los dientes del cuarto cuadrante
		*/
		public function cuartoCuadrante()
		{			
			var posx:int = 0;
			var posy:int = 280;
			var cantsepara:int = 40;
			var tamano:int = 34;	
			
			var posxCheck:int = 12;
			var posyCheck:int = -37;
			
			var posxLabel:int = 6;
			var posyLabel:int = 15;
			
			//48 47 46 45 44 43 42 41
			pintarDiente(48,30,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = 30 + cantsepara;
			
			pintarDiente(47,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(46,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(45,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(44,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(43,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(42,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(41,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
																												
			//85 84 83 82 81
			
			posy = 210;
			posxLabel= 6;
			posyLabel= 15;
			
			pintarDiente(85,150,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = 150 + cantsepara;
			
			pintarDiente(84,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(83,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(82,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(81,posx,posy,tamano,cantsepara,posxCheck,posyCheck,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;				
		}
		
		/**
		realiza una operacion matematica sobre el campo 
		Numero de Superficies Dentales Seleccionadas
		*/
		public function operNumSuperDentSelec(valor:int,operacion:String):void
		{
			switch(operacion)
			{
				case "sum":
					this.numSuperDentSelec = this.numSuperDentSelec + valor;
				break;
				case "rest":
					this.numSuperDentSelec = this.numSuperDentSelec - valor;
				break;
			}
			
			calcularPorcentajePlaca();			
		}
		
		/**
		realiza una operacion matematica sobre el campo 
		Numero de Superficies Dentales Presentes
		*/
		public function operNumSuperDentPrese(valor:int,operacion:String):void
		{
			switch(operacion)
			{
				case "sum":
					this.numSuperDentPrese = this.numSuperDentPrese + valor;
				break;
				case "rest":
					this.numSuperDentPrese = this.numSuperDentPrese - valor;
				break;
			}

			calcularPorcentajePlaca();
		}			
		
		/**
		Calcula el porcentaje de placa
		*/
		public function calcularPorcentajePlaca():void
		{
			var porcentaje:Number = 0;			
			if(this.numSuperDentPrese > 0)
			{
				porcentaje = (this.numSuperDentSelec / this.numSuperDentPrese)*100;				
			}						
			
            labelporcentaje.text = porcentaje.toFixed(2);
			ct_indplaca.addChild(labelporcentaje);
			
			getInterpretacion(porcentaje);
		}
		
		/**
		Devuelve la posicion donde fue guardado el diente		
		*/
		public function getPosNumeroDiente(param_diente:int):int
		{
			if(param_diente > 0)
			{
				for(var i:Number = 0; i < arrayDientes.length; i++)
				{				
					if(arrayDientes[i][0] == param_diente+"")
						return (int(arrayDientes[i][1]+""));
				}
			}
			
			return Constantes.codigoNuncaValido;
		}
		
		/**
		Devuelve la interpretacion a partir del porcentaje
		*/
		public function getInterpretacion(porcentaje:Number):void
		{
			labelinterpretacion.text = "";
			for each(var nodo:XML in arrayRangosXml.elements())
			{
				if(porcentaje >= Number(nodo.rangoinicial) && 
					porcentaje <= Number(nodo.rangofinal))
				labelinterpretacion.text = nodo.descripcion;							
			}

			ct_indplaca.addChild(labelinterpretacion);			
		}
		
		/**
		Genera el contenido del Movie Clip en un mapa de pixels		
		*/
		function generarmapapixelEvento(e:MouseEvent):void
		{
			try
			{
				var jpgSource:BitmapData = new BitmapData (700,350,true);
				jpgSource.draw(this.ct_indplaca);
				
				var jpgEncoder:JPGEncoder = new JPGEncoder(85);
				var jpgStream:ByteArray = jpgEncoder.encode(jpgSource);
				
				var req:MultipartURLLoader = new MultipartURLLoader();
				req.addEventListener(Event.COMPLETE, onReady);
				req.addFile(jpgStream,'indiceplaca_'+indicadorImagen, 'file');
				req.load(this.rutaImagen+"");
				crearBtnActualizar();
				jpgStream.clear();
				jpgSource.dispose();
			}
			catch(err:Error)
			{
				ExternalInterface.call("outErrorSwf",err.toString()+" "+err.message+" "+err.name);
				trace(err.toString());
			}
			
			getNombreImgGenerado();
		}

		/**
		 * Cerrar la ventana de popup cuando termine de subir a imagen
		 */
		function onReady(e:Event):void
		{
			ExternalInterface.call("obtenerXmlIndicePlaca",null);
		}

		/**
		*/
		function getNombreImgGenerado():void
		{
			ExternalInterface.call("setNomImgIndicePlaca","indiceplaca_"+indicadorImagen);
		}
		
		/**
		Carga El indice de Placa a partir de un xml
		*/		
		public function cargarIndicePlacaXML():void
		{
			if(xmlIndicePlaca != "")
			{
				var arrayXml:XML = new XML(xmlIndicePlaca);
				var parentObjP:Object = this.ct_indplaca as Object;	
				for each(var nodo:XML in arrayXml.elements())
				{
					if(nodo.@pieza.toString() == (Constantes.codigoNuncaValido+""))
					{
						this.labelporcentaje.text = nodo.porcentaje;
						this.labelinterpretacion.text = nodo.interpretacion;						
					}
					else
					{
						var index:int = 0;
						index = getPosNumeroDiente(nodo.@pieza);

						if(index > 0)
						{
							if(nodo.@indicativo == Constantes.acronimoAusente)
							{
								parentObjP.getChildAt(index).getAusente.selected = true;
								parentObjP.getChildAt(index).evaluarAusente(null);
							}
							else
							{
								for(var i=0; i<nodo.superficie.length(); i++)
								{
									if(nodo.superficie[i].@sector == Constantes.codigoSectorDiente1)
									{
										parentObjP.getChildAt(index).getVestibular.setMarcado = Constantes.acronimoNo;
										parentObjP.getChildAt(index).getVestibular.pintar(null);
									}
									
									if(nodo.superficie[i].@sector == Constantes.codigoSectorDiente2)
									{
										parentObjP.getChildAt(index).getMesial.setMarcado = Constantes.acronimoNo;
										parentObjP.getChildAt(index).getMesial.pintar(null);
									}
									
									if(nodo.superficie[i].@sector == Constantes.codigoSectorDiente3)
									{
										parentObjP.getChildAt(index).getLingual.setMarcado = Constantes.acronimoNo;
										parentObjP.getChildAt(index).getLingual.pintar(null);
									}
									
									if(nodo.superficie[i].@sector == Constantes.codigoSectorDiente4)
									{
										parentObjP.getChildAt(index).getDistal.setMarcado = Constantes.acronimoNo;
										parentObjP.getChildAt(index).getDistal.pintar(null);
									}
								}
							}
						}
					}
				}
			}
			
			xmlIndicePlaca = "";
		}
		
		/**
		Genera una cadena xml donde se encuentra el estado
		actual del los dientes
		*/
		public function generarXML():String
		{
			var cadena:String = "";
			var index:Number = 0;
			var parentObjP:Object = this.ct_indplaca as Object;	

			cadena = '<contenido>';
			
			for(var i:Number=0; i<arrayDientes.length; i++)
			{				
				index = arrayDientes[i][1];
				if(index > 0)
				{
					if(parentObjP.getChildAt(index).getAusente.selected)
					{
						cadena+="<diente pieza='"+parentObjP.getChildAt(index).getNumeroDiente+"' indicativo='"+Constantes.acronimoAusente+"'></diente>";
					}
					else if(parentObjP.getChildAt(index).esDienteUsado())
					{
						cadena+=
								"<diente "+
								"pieza='"+parentObjP.getChildAt(index).getNumeroDiente+"' "+
								"indicativo='"+Constantes.acronimoPlaca+"' "+				
								">";
								
						if(parentObjP.getChildAt(index).getVestibular.getMarcado == Constantes.acronimoSi)
						{
							cadena+= 
									"<superficie "+
										"codigo = '"+parentObjP.getChildAt(index).getVestibular.getCodigo+"' "+
										"nombre = '"+parentObjP.getChildAt(index).getVestibular.getNombre+"' "+
										"sector = '"+Constantes.codigoSectorDiente1+"' "+																			
									"></superficie>";
						}
						
						if(parentObjP.getChildAt(index).getMesial.getMarcado == Constantes.acronimoSi)
						{
							cadena+= 
									"<superficie "+
										"codigo = '"+parentObjP.getChildAt(index).getMesial.getCodigo+"' "+
										"nombre = '"+parentObjP.getChildAt(index).getMesial.getNombre+"' "+
										"sector = '"+Constantes.codigoSectorDiente2+"' "+																			
									"></superficie>";
						}
						
						
						if(parentObjP.getChildAt(index).getLingual.getMarcado == Constantes.acronimoSi)
						{
							cadena+= 
									"<superficie "+
										"codigo = '"+parentObjP.getChildAt(index).getLingual.getCodigo+"' "+
										"nombre = '"+parentObjP.getChildAt(index).getLingual.getNombre+"' "+
										"sector = '"+Constantes.codigoSectorDiente3+"' "+																			
									"></superficie>";
						}
						
						if(parentObjP.getChildAt(index).getDistal.getMarcado == Constantes.acronimoSi)
						{
							cadena+= 
									"<superficie "+
										"codigo = '"+parentObjP.getChildAt(index).getDistal.getCodigo+"' "+
										"nombre = '"+parentObjP.getChildAt(index).getDistal.getNombre+"' "+
										"sector = '"+Constantes.codigoSectorDiente4+"' "+																			
									"></superficie>";
						}
						
						cadena+="</diente>";		
					}
				}
			}
			
			cadena+=	"<diente pieza='"+Constantes.codigoNuncaValido+"'> "+
							"<numSuperDentSelec>'"+numSuperDentSelec+"'</numSuperDentSelec>"+
							"<activoDienteAdulto>'"+activoDienteAdulto+"'</activoDienteAdulto>"+
							"<activoDienteNino>'"+activoDienteNino+"'</activoDienteNino>"+
							"<porcentaje>'"+labelporcentaje.text+"'</porcentaje>"+
							"<interpretacion>'"+labelinterpretacion.text+"'</interpretacion>"+
						"</diente>";
			
			cadena += "</contenido>";
			
			//Envia el contenido xml
			//trace(cadena)
			return cadena;
		}
	}	
}