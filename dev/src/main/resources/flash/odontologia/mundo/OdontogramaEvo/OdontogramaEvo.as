package mundo.OdontogramaEvo
{
	import flash.display.*;	
	import fl.controls.*;
	import flash.display.MovieClip;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.text.AntiAliasType;
	import flash.external.ExternalInterface;
	import fl.data.DataProvider;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.ByteArray;
	import flash.net.URLRequestMethod;
	import flash.net.URLRequest;
	import flash.net.URLRequestHeader;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.utils.Timer;
	import flash.events.TimerEvent;
	
	import mundo.OdontogramaEvo.DienteOdtEvo;
	import util.general.Constantes;
	
	import util.corel.src.com.adobe.images.JPGEncoder;
	import util.corel.src.com.adobe.UploadPostHelper;
	import util.corel.src.com.adobe.MultipartURLLoader;
	
	public class OdontogramaEvo extends MovieClip
	{
		// Constants:
		// Public Properties:

		// Private Properties:
		private var activoDienteAdulto:String = "" ;
		private var activoDienteNino:String = "";
		private var dienteDetalle:DienteOdtEvo;
		private var xmlPosInicial:XML;
		public var dpHallazgosSuper:DataProvider;
		public var dpHallazgosDiente:DataProvider;
		public var rutaImagen:String;
	
		// Initialization:
		public function OdontogramaEvo()
		{
			xmlPosInicial = new XML();
			rutaImagen = "";
			
			ExternalInterface.addCallback("setSwfActivoDienteAdulto",setActivoDienteAdulto);			
			ExternalInterface.addCallback("setSwfActivoDienteNino",setActivoDienteNino);			
			ExternalInterface.addCallback("setSwfXmlPosInicial",setXmlPosInicial);
			ExternalInterface.addCallback("setSwfPintarOdontograma",pintarOdontograma);			
			ExternalInterface.addCallback("setSwfInfoImagen",setInfoImagen);
			ExternalInterface.addCallback("setSwfImagenDienteSuper",setImagenDienteSuper);
			ExternalInterface.addCallback("outSwfImagenJPG",outImagenJPG);

//			datosDemostracion();
//			datosPrueba();
		}

		/**
		*/
		public function crearBtnConfirmar():void
		{
			//this.btn_confirmar.label = "CONFIRMAR";					
			//this.btn_confirmar.useHandCursor = true;			
			//ddChild(btn_confirmar);
		}
		
		/**
		*/
		public function outImagenJPG():Boolean
		{
//			ExternalInterface.call("outErrorSwf","Si entra al flash");
			return generarmapapixelEvento();
			
		}
		
		/**
		Pinta el odontograma
		*/
		public function pintarOdontograma()
		{
			
			//this.btn_confirmar.addEventListener(MouseEvent.delta,confirmarOdontograma);

			trace("pintar odontograma");

			primerCuadrante();
			segundoCuadrante();
			tercerCuadrante();
			cuartoCuadrante();
			
			iniciarOdontogramaDeXML();		
			
			//generarmapapixelEvento();
			
		}
	
		// Public Methods:
		// Protected Methods:
		
		/**
		Genera el xml del odontograma
		*/
		private function confirmarOdontograma(e:MouseEvent)		
		{
			trace(e);
		}
		
		
		/**
		Pinta un diente en el movie clip deacuerdo a los parametros			
		*/
		private function pintarDiente(
									 numero:int,
									 posx:int,
									 posy:int,
									 tamano:int,
									 cantsepara:int,
									 posxlabel:int,
									 posylabel:int,
									 activo:String,
									 excluido:String):void
		{
			var diente:DienteOdtEvo = new DienteOdtEvo(numero,activo,excluido);
			diente.x = posx;
			diente.y = posy;
			diente.height = tamano;
			diente.width = tamano;
			
			//registra la posiciòn del diente en el movie clip
			var myLabel:Label =  new Label();			
			myLabel.text = numero+"";		
			myLabel.autoSize = TextFieldAutoSize.NONE;
			myLabel.move((diente.x - posxlabel),(posy + posylabel));			

//			diente.buttonMode = true;
//			diente.useHandCursor = true;
			addChild(myLabel);
			
			if(activo != Constantes.acronimoSi)			
			{
				diente.alpha = 0.5;
			}
				
			addChild(diente);			
			
			diente.iniciarSelect();
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
			pintarDiente(18,30,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = 30 + cantsepara;
			
			pintarDiente(17,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(16,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(15,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(14,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(13,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(12,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(11,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
											
			//55 54 53 52 51
			
			posy = 125;
			posxLabel= 9;
			posyLabel= -35;
			
			pintarDiente(55,150,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);
			posx = 150 + cantsepara;
			
			pintarDiente(54,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(53,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(52,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(51,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
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
			pintarDiente(21,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(22,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(23,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(24,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(25,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(26,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(27,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(28,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;			
			
			//61 62 63 64 65
			
			posx = 390;
			posy = 125;			
			posxLabel= 9;
			posyLabel= -35;
			
			pintarDiente(61,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(62,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(63,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(64,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(65,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
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
			pintarDiente(31,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);
			posx = posx + cantsepara;
			
			pintarDiente(32,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(33,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(34,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(35,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(36,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(37,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(38,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;	
						
			//71 72 73 74 75 			
			posx = 390;
			posy = 210;		
			
			posxCheck = 12;
			posyCheck = -37;
			
			posxLabel= 6;
			posyLabel= 15;
			
			pintarDiente(71,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(72,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(73,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(74,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(75,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
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
			pintarDiente(48,30,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = 30 + cantsepara;
			
			pintarDiente(47,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(46,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(45,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(44,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(43,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(42,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
			posx = posx + cantsepara;
			
			pintarDiente(41,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo);			
																												
			//85 84 83 82 81
			
			posy = 210;
			posxLabel= 6;
			posyLabel= 15;
			
			pintarDiente(85,150,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = 150 + cantsepara;
			
			pintarDiente(84,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(83,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(82,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;
			
			pintarDiente(81,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi);			
			posx = posx + cantsepara;				
		}

		/**
		 * Iniciar el odontograma basandose en un XML
		 */
		private function iniciarOdontogramaDeXML()
		{			
			for each(var dienteNodo:XML in xmlPosInicial.elements())
			{
				if(Number(dienteNodo.@pieza) > 0)
				{
					var diente:DienteOdtEvo = this.getChildByName(dienteNodo.@pieza) as DienteOdtEvo;

					for each(var superficieNodo:XML in dienteNodo.elements())
					{
						if(Number(superficieNodo.@codigo) > 0)
						{
							if(superficieNodo.@sector == Constantes.codigoSectorDiente1)
							{
								diente.getVestibular.setCodigo = superficieNodo.@codigo;
								diente.getVestibular.setNombreSector = superficieNodo.@nombre;
								diente.getVestibular.setPathImagen = superficieNodo.hallazgo.path;
								diente.getVestibular.setColorS = superficieNodo.hallazgo.color;
								diente.getVestibular.dibujarHallazgo(true);
							}
							
							if(superficieNodo.@sector == Constantes.codigoSectorDiente2)
							{
								diente.getMesial.setCodigo = superficieNodo.@codigo;
								diente.getMesial.setNombreSector = superficieNodo.@nombre;									
								diente.getMesial.setPathImagen = superficieNodo.hallazgo.path;	
								diente.getMesial.setColorS = superficieNodo.hallazgo.color;
								diente.getMesial.dibujarHallazgo(true);
							}
							
							if(superficieNodo.@sector == Constantes.codigoSectorDiente3)
							{
								diente.getLingual.setCodigo = superficieNodo.@codigo;
								diente.getLingual.setNombreSector = superficieNodo.@nombre;									
								diente.getLingual.setPathImagen = superficieNodo.hallazgo.path;
								diente.getLingual.setColorS = superficieNodo.hallazgo.color;								
								diente.getLingual.dibujarHallazgo(true);
							}
							
							if(superficieNodo.@sector == Constantes.codigoSectorDiente4)
							{
								diente.getDistal.setCodigo = superficieNodo.@codigo;
								diente.getDistal.setNombreSector = superficieNodo.@nombre;
								diente.getDistal.setPathImagen = superficieNodo.hallazgo.path;
								diente.getDistal.setColorS = superficieNodo.hallazgo.color;								
								diente.getDistal.dibujarHallazgo(true);
							}
							
							if(superficieNodo.@sector == Constantes.codigoSectorDiente5)
							{
								diente.getOclusal.setCodigo = superficieNodo.@codigo;
								diente.getOclusal.setNombreSector = superficieNodo.@nombre;
								diente.getOclusal.setPathImagen = superficieNodo.hallazgo.path;
								diente.getOclusal.setColorS = superficieNodo.hallazgo.color;
								diente.getOclusal.dibujarHallazgo(true);
							}
						}
						else
						{
							diente.setPathImagen = superficieNodo.hallazgo.path;
							diente.setColorS = superficieNodo.hallazgo.color;
							diente.dibujarHallazgo(true);
						}
					}
				}
			}
			
			xmlPosInicial = new XML();
		}
		
		/**
		*/
		public function setActivoDienteAdulto(param:String)
		{
			this.activoDienteAdulto = param;
		}
		
		/**
		*/
		public function setActivoDienteNino(param:String)
		{
			this.activoDienteNino = param;
		}
		
		/**
		*/
		public function setXmlPosInicial(posiciones:String)
		{
			//Analisis de variables externar
			if(posiciones == "" || posiciones == "null" || posiciones.length <= 0 || posiciones == "undefined")
			{
				posiciones  = "";
			}
			
			xmlPosInicial = new XML(posiciones);
		}
		
		/**
		Genera el contenido del Movie Clip en un mapa de pixels		
		*/
		function generarmapapixelEvento():Boolean
		{
			try
			{
				var jpgSource:BitmapData = new BitmapData (700,350,true);
				jpgSource.draw(this);
				
				var jpgEncoder:JPGEncoder = new JPGEncoder(85);
				var jpgStream:ByteArray = jpgEncoder.encode(jpgSource);
				
				
				var urlRequest:URLRequest = new URLRequest(this.rutaImagen);

				// With this line of code, the call to urlLoader.load() throws the following security exception:
				// 'SecurityError: Error #2176: Certain actions, such as those that display a pop-up window, may only be invoked upon user interaction, for example by a mouse click or button press.'
				
				urlRequest.method = URLRequestMethod.POST;
//				ExternalInterface.call("outErrorSwf",rutaImagen);
				urlRequest.data = UploadPostHelper.getPostData(rutaImagen, jpgStream);
				urlRequest.requestHeaders.push(new URLRequestHeader('Cache-Control', 'no-cache'));
				urlRequest.requestHeaders.push(new URLRequestHeader('Content-Type', 'multipart/form-data; boundary=' + UploadPostHelper.getBoundary()));
				
				var urlLoader:URLLoader = new URLLoader();
				urlLoader.dataFormat = URLLoaderDataFormat.BINARY;
				//urlLoader.dataFormat = URLLoaderDataFormat.TEXT;
				//urlLoader.addEventListener(Event.COMPLETE, onUploadComplete);
				//urlLoader.addEventListener(IOErrorEvent.IO_ERROR, onUploadError);
				
				urlLoader.load(urlRequest);
				
				return true;
				
				/*
				var req:MultipartURLLoader = new MultipartURLLoader();
				req.addFile(jpgStream,'odoevo_', 'file');
				ExternalInterface.call("outErrorSwf","envio el request");
				req.load(this.rutaImagen);
				ExternalInterface.call("outErrorSwf","limpio el biffer");
				jpgStream.clear();
				ExternalInterface.call("outErrorSwf","unload la imagen");
				jpgSource.dispose();*/
			}
			catch(err:Error)
			{
				ExternalInterface.call("outErrorSwf",err.toString()+" errores "+err.message+" "+err.name);
				trace(err.toString());
			}
			return false;
		}
		
		/**
		 * Actualiza la informaciòn para la generaciòn de la imagen generada
		 */
		public function setInfoImagen(ruta:String)
		{
			rutaImagen = ruta;
		}
		
		/**
		 *
		 */
		public function setImagenDienteSuper(
											 esDiente:Boolean,
											 codigoDiente:String,
											 codigoSuperficie:int,
											 pathImagen:String,
											 colorParam:String
											 )
		{
			var diente:DienteOdtEvo = this.getChildByName(codigoDiente) as DienteOdtEvo;
						
			if(esDiente)
			{
				if(pathImagen != "" || colorParam!= "")
				{
					diente.setPathImagen = pathImagen;
					diente.setColorS = colorParam;
					diente.dibujarHallazgo(true);
				}
				else
				{
					diente.actualizarSuperficie("","");
					diente.vestibular.actualizarSuperficie("","");
					diente.mesial.actualizarSuperficie("","");
					diente.lingual.actualizarSuperficie("","");
					diente.distal.actualizarSuperficie("","");
					diente.oclusal.actualizarSuperficie("","");
				}
			}
			else
			{
				if(codigoSuperficie == Constantes.codigoSectorDiente1)
				{
					if(pathImagen != "" || colorParam!= "")
					{
						diente.getVestibular.setPathImagen = pathImagen;
						diente.getVestibular.setColorS = colorParam;
						diente.getVestibular.dibujarHallazgo(true);
					}
					else					
					{
						diente.vestibular.actualizarSuperficie("","");
					}
					
				}
				else if(codigoSuperficie == Constantes.codigoSectorDiente2)
				{
					if(pathImagen != "" || colorParam!= "")
					{
						diente.getMesial.setPathImagen = pathImagen;
						diente.getMesial.setColorS = colorParam;
						diente.getMesial.dibujarHallazgo(true);
					}
					else					
					{
						diente.mesial.actualizarSuperficie("","");
					}
				}
				else if(codigoSuperficie == Constantes.codigoSectorDiente3)
				{
					if(pathImagen != "" || colorParam!= "")
					{
						diente.getLingual.setPathImagen = pathImagen;
						diente.getLingual.setColorS = colorParam;						
						diente.getLingual.dibujarHallazgo(true);
					}
					else					
					{
						diente.lingual.actualizarSuperficie("","");
					}
				}
				else if(codigoSuperficie == Constantes.codigoSectorDiente4)
				{					
					if(pathImagen != "" || colorParam!= "")
					{
						diente.getDistal.setPathImagen = pathImagen;
						diente.getDistal.setColorS = colorParam;						
						diente.getDistal.dibujarHallazgo(true);
					}
					else
					{
						diente.distal.actualizarSuperficie("","");
					}
				}
				else if(codigoSuperficie == Constantes.codigoSectorDiente5)
				{
					if(pathImagen != "" || colorParam!= "")
					{
						diente.getOclusal.setPathImagen = pathImagen;
						diente.getOclusal.setColorS = colorParam;						
						diente.getOclusal.dibujarHallazgo(true);
					}
					else	
					{
						diente.oclusal.actualizarSuperficie("","");
					}
				}
			}
		}
	
	
		/**
		 * Función que genera datos de demostración
		 */
		private function datosDemostracion():void
		{
						// DATOS DE PRUEBA
			//*************************************
			
			this.activoDienteAdulto = 'S';
			this.activoDienteNino = 'S';
			
			var b:String = 
				'<contenido>'+
					'<diente pieza = "12" >'+
						'<superficie codigo = "1" nombre = "oclusal" sector = "1">'+
							'<hallazgo>'+
								'<codigo>1</codigo>'+
								'<descripcion>Hola</descripcion>'+
								'<color>S</color>'+
								'<path></path>'+
								'<convencion></convencion>'+
							'</hallazgo>'+
						'</superficie>'+
						'<superficie codigo = "1" nombre = "mesial" sector = "3">'+
							'<hallazgo>'+
								'<codigo>1</codigo>'+
								'<descripcion>Hola</descripcion>'+
								'<path>1.jpg</path>'+
								'<convencion></convencion>'+
							'</hallazgo>'+
						'</superficie>'+
						'<superficie codigo = "1" nombre = "oclusal" sector = "5">'+
							'<hallazgo>'+
								'<codigo>1</codigo>'+
								'<descripcion>Hola</descripcion>'+
								'<path>1.jpg</path>'+
								'<convencion></convencion>'+
							'</hallazgo>'+
						'</superficie>'+
					'</diente>'+
					'<diente pieza = "11">'+
						'<superficie codigo = "1" nombre = "vestibular" sector = "1">'+
							'<hallazgo>'+
								'<codigo>1</codigo>'+
								'<descripcion>Hola</descripcion>'+
								'<path>1.jpg</path>'+
								'<convencion></convencion>'+
							'</hallazgo>'+
						'</superficie>'+
						'<superficie codigo = "1" nombre = "mesial" sector = "3">'+
							'<hallazgo>'+
								'<codigo>1</codigo>'+
								'<descripcion>Hola</descripcion>'+
								'<path>1.jpg</path>'+
								'<convencion></convencion>'+
							'</hallazgo>'+
						'</superficie>'+
						'<superficie codigo = "1" nombre = "oclusal" sector = "5">'+
							'<hallazgo>'+
								'<codigo>1</codigo>'+
								'<descripcion>Hola</descripcion>'+
								'<path></path>'+
								'<color>S</color>'+
								'<convencion></convencion>'+
							'</hallazgo>'+
						'</superficie>'+
						'</diente>'+
						'<diente pieza = "13" >'+
							'<superficie codigo = "-1" nombre = "oclusal" sector = "1">'+
								'<hallazgo>'+
									'<codigo>1</codigo>'+
									'<descripcion>Hola</descripcion>'+
									'<color>S</color>'+
									'<path></path>'+
									'<convencion></convencion>'+
								'</hallazgo>'+
							'</superficie>'+
						'</diente>'+
					'</contenido>';
			setXmlPosInicial(b);
			pintarOdontograma();

		}
		
		/**
		 * Función que genera datos de prueba
		 */
		private function datosPrueba():void
		{
			var prueba:String ="<contenido><diente pieza = '33'><superficie codigo = '7' nombre = 'Vestibular' sector = '3' ><hallazgo><codigo>0</codigo><descripcion></descripcion><path>1.jpg</path><convencion></convencion><color></color></hallazgo></superficie></diente><diente pieza = '32'><superficie codigo = '-1' nombre = 'diente'><hallazgo><color>S</color></hallazgo></superficie></diente><diente pieza = '31'><superficie codigo = '-1' nombre = 'diente'><hallazgo><path>2.jpg</path></hallazgo></superficie></diente></contenido>";
			this.activoDienteAdulto = 'S';
			this.activoDienteNino = 'S';
			setXmlPosInicial(prueba);
			pintarOdontograma();
		}
	}	
}