package mundo.Odontograma
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
	import flash.events.MouseEvent;
	import flash.events.Event;
	import flash.utils.ByteArray;
	import flash.net.URLRequestMethod;
	import flash.net.URLRequest;
	import flash.net.URLRequestHeader;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	
	import mundo.Odontograma.DienteOdt;
	import util.general.Constantes;
	
	import util.corel.src.com.adobe.images.JPGEncoder;
	import util.corel.src.com.adobe.UploadPostHelper;
	import util.corel.src.com.adobe.MultipartURLLoader;
	
	public class OdontogramaDx extends MovieClip
	{
		// Constants:
		// Public Properties:

		// Private Properties:
		private var activoDienteAdulto:String = "" ;
		private var activoDienteNino:String = "";
		private var dienteDetalle:DienteOdt;
		private var xmlPosInicial:XML;
		private var dpHallazgosSuper:DataProvider;
		private var dpHallazgosDiente:DataProvider;
		public var arrayImagenes:Array;
		public var rutaImagen:String;
		private var odontogramaEditable:Boolean=true;
	
		// Initialization:
		public function OdontogramaDx() 
		{
			dpHallazgosSuper = new DataProvider();
			dpHallazgosDiente = new DataProvider();
			arrayImagenes = new Array();
			xmlPosInicial = new XML();
			rutaImagen = "";
			
			ExternalInterface.addCallback("setSwfActivoDienteAdulto",setActivoDienteAdulto);			
			ExternalInterface.addCallback("setSwfActivoDienteNino",setActivoDienteNino);			
			ExternalInterface.addCallback("setSwfHallazgosSuperficie",setHallazgosSuperficie);			
			ExternalInterface.addCallback("setSwfHallazgosDiente",setHallazgosDiente);
			ExternalInterface.addCallback("setSwfXmlPosInicial",setXmlPosInicial);
			ExternalInterface.addCallback("setSwfPintarOdontograma",pintarOdontograma);
			ExternalInterface.addCallback("setSwfInfoImagen",setInfoImagen);
			ExternalInterface.addCallback("setSwfOdontogramaEditable", setOdontogramaEditable);
			ExternalInterface.addCallback("setSwfConfirmarOdontograma", confirmarOdontograma);
			
			this.btn_confirmar.label = "CONFIRMAR";
			this.btn_confirmar.useHandCursor = true;			
			
			
/*
			Se debe descomentar pintarOdontograma() para hacer pruebas
*/
			//************************************* 
			//      DATOS DE PRUEBA, COMENTAR ESTO
/*
			this.activoDienteAdulto = 'S';
			this.activoDienteNino = 'S';
			var hallSup="<Contenido>"+
							"<Elemento>"+
								"<codigo>-1</codigo>"+
								"<descripcion>Seleccione</descripcion>"+"<imagen>"+
								"</imagen>"+
								"<borde></borde>"+
							"</Elemento>"+
							"<Elemento>"+
								"<codigo>41</codigo>"+
								"<descripcion>41 PRUEBA SUBA</descripcion>"+
								"<imagen>1.jpg</imagen>"+
								"<convencion>155</convencion>"+
								"<borde>0xffffff</borde>"+
							"</Elemento>"+
							"<Elemento>"+
								"<codigo>43</codigo>"+
								"<descripcion>43 PRUEBITAS</descripcion><imagen>/qa_incidencias_1.0.0/imagenesOdontologia/convencion/convencion_22.jpg</imagen><convencion>22</convencion><borde>0xffffff</borde></Elemento><Elemento><codigo>6</codigo><descripcion>6 SUPERFICIE CARIADA</descripcion><imagen>/qa_incidencias_1.0.0/imagenesOdontologia/convencion/convencion_116.jpg</imagen><convencion>116</convencion><borde>0xffffff</borde></Elemento><Elemento><codigo>40</codigo><descripcion>40 SUPERFICIE FISURADA</descripcion><imagen>/qa_incidencias_1.0.0/imagenesOdontologia/convencion/convencion_118.jpg</imagen><convencion>118</convencion><borde>0xffffff</borde></Elemento></Contenido>";
			setHallazgosSuperficie(hallSup);
			
			
			var hallDie="<Contenido><Elemento><codigo>-1</codigo><descripcion>Seleccione</descripcion><imagen></imagen><borde></borde></Elemento>"+
							"<Elemento><codigo>1</codigo><descripcion>1 CARIES DE LA DENTINA</descripcion><imagen>/qa_incidencias_1.0.0/imagenesOdontologia/convencion/convencion_162.jpg</imagen><convencion>162</convencion><borde>0xff0000</borde></Elemento>"+
							"<Elemento><codigo>2</codigo><descripcion>2 CARIES DEL CEMENTO</descripcion><imagen>/qa_incidencias_1.0.0/imagenesOdontologia/convencion/convencion_41.jpg</imagen><convencion>41</convencion><borde>0xff0000</borde></Elemento>"+
							"<Elemento><codigo>3</codigo><descripcion>3 DIENTE EXTRAIDO</descripcion><imagen>2.jpg</imagen><convencion>22</convencion><borde>0xffffff</borde></Elemento>"+
							"<Elemento><codigo>5</codigo><descripcion>5 DIENTE SANO</descripcion><imagen>3.jpg</imagen><convencion>1</convencion><borde>0xffffff</borde></Elemento>"+
							"<Elemento><codigo>42</codigo><descripcion>42 PROBANDO</descripcion><imagen>1w.jpg</imagen><convencion>159</convencion><borde>0xffffff</borde></Elemento></Contenido>";
			setHallazgosDiente(hallDie);
			
			var b:String = 
						"<contenido>"+
							"<diente pieza = '12' >"+
								"<superficie codigo = '1' sector = '1' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo><descripcion>Hola</descripcion>"+
										"<descripcion>Hola</descripcion>"+
										"<path>2.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '2' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo><descripcion>Hola</descripcion>"+
										"<descripcion>Hola</descripcion>"+
										"<path>2.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '3' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>2</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>2.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '4' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo><descripcion>Hola</descripcion>"+
										"<descripcion>Hola</descripcion>"+
										"<path>2.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '5' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>2.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
							"</diente>"+
							"<diente pieza = '11' >"+
								"<superficie codigo = '1' sector = '1' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>2</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>1w.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '3' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>1w.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '2' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>1w.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
								"<superficie codigo = '1' sector = '4' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>1</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>1w.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
							"</diente>"+
							"<diente pieza = '15'>"+
								"<superficie codigo = '-1' modificable='false'>"+
									"<hallazgo>"+
										"<codigo>2</codigo>"+
										"<descripcion>Hola</descripcion>"+
										"<path>4.jpg</path>"+
										"<convencion></convencion>"+
										"<borde>0xFF00FF</borde>"+
									"</hallazgo>"+
								"</superficie>"+
							"</diente>"+
						"</contenido>";

			setXmlPosInicial(b);
			pintarOdontograma();
*/

			//*************************************
			
//			ExternalInterface.call("iniciarOdontograma");
			
		}
		
		/**
		*/
		public function crearBtnConfirmar()
		{
			this.btn_confirmar.label = "CONFIRMAR";					
			this.btn_confirmar.useHandCursor = true;			
			addChild(btn_confirmar);
		}
		
		
		/**
		Pinta el odontograma
		*/
		public function pintarOdontograma()
		{
			
			if(odontogramaEditable)
			{
				this.btn_confirmar.addEventListener(MouseEvent.CLICK,confirmarOdontogramaEvent);
			}
			else
			{
				this.removeChild(btn_confirmar);
			}
			
			
			primerCuadrante();
			segundoCuadrante();
			tercerCuadrante();
			cuartoCuadrante();
			
			iniciarOdontogramaDeXML();
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
									 excluido:String,
									 cuadrante:int):void
		{
			if(!odontogramaEditable)
			{
				activo=Constantes.acronimoNo;
			}
			var diente:DienteOdt = new DienteOdt(numero,activo,excluido,cuadrante);
			diente.x = posx;
			diente.y = posy;
			diente.height = tamano;
			diente.width = tamano;
			
			//registra la posiciòn del diente en el movie clip
			var myLabel:Label =  new Label();			
			myLabel.text = numero+"";		
			myLabel.autoSize = TextFieldAutoSize.NONE;
			myLabel.move((diente.x - posxlabel),(posy + posylabel));			

			addChild(myLabel);
			
			if(activo != Constantes.acronimoSi && odontogramaEditable)
			{
				diente.buttonMode = true;
				diente.useHandCursor = true;
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
			pintarDiente(18,30,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = 30 + cantsepara;
			
			pintarDiente(17,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = posx + cantsepara;
			
			pintarDiente(16,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = posx + cantsepara;
			
			pintarDiente(15,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = posx + cantsepara;
			
			pintarDiente(14,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = posx + cantsepara;
			
			pintarDiente(13,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = posx + cantsepara;
			
			pintarDiente(12,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
			posx = posx + cantsepara;
			
			pintarDiente(11,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,1);
											
			//55 54 53 52 51
			
			posy = 125;
			posxLabel= 9;
			posyLabel= -35;
			
			pintarDiente(55,150,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,1);
			posx = 150 + cantsepara;
			
			pintarDiente(54,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,1);			
			posx = posx + cantsepara;
			
			pintarDiente(53,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,1);			
			posx = posx + cantsepara;
			
			pintarDiente(52,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,1);			
			posx = posx + cantsepara;
			
			pintarDiente(51,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,1);			
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
			pintarDiente(21,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);
			posx = posx + cantsepara;
			
			pintarDiente(22,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);
			posx = posx + cantsepara;
			
			pintarDiente(23,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);
			posx = posx + cantsepara;
			
			pintarDiente(24,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);
			posx = posx + cantsepara;
			
			pintarDiente(25,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);			
			posx = posx + cantsepara;
			
			pintarDiente(26,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);			
			posx = posx + cantsepara;
			
			pintarDiente(27,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);			
			posx = posx + cantsepara;
			
			pintarDiente(28,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,2);			
			posx = posx + cantsepara;			
			
			//61 62 63 64 65
			
			posx = 390;
			posy = 125;			
			posxLabel= 9;
			posyLabel= -35;
			
			pintarDiente(61,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,2);			
			posx = posx + cantsepara;
			
			pintarDiente(62,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,2);			
			posx = posx + cantsepara;
			
			pintarDiente(63,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,2);			
			posx = posx + cantsepara;
			
			pintarDiente(64,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,2);			
			posx = posx + cantsepara;
			
			pintarDiente(65,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,2);			
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
			pintarDiente(31,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);
			posx = posx + cantsepara;
			
			pintarDiente(32,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;
			
			pintarDiente(33,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;
			
			pintarDiente(34,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;
			
			pintarDiente(35,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;
			
			pintarDiente(36,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;
			
			pintarDiente(37,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;
			
			pintarDiente(38,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,3);			
			posx = posx + cantsepara;	
						
			//71 72 73 74 75 			
			posx = 390;
			posy = 210;		
			
			posxCheck = 12;
			posyCheck = -37;
			
			posxLabel= 6;
			posyLabel= 15;
			
			pintarDiente(71,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,3);			
			posx = posx + cantsepara;
			
			pintarDiente(72,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,3);			
			posx = posx + cantsepara;
			
			pintarDiente(73,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,3);			
			posx = posx + cantsepara;
			
			pintarDiente(74,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,3);			
			posx = posx + cantsepara;
			
			pintarDiente(75,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,3);			
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
			pintarDiente(48,30,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = 30 + cantsepara;
			
			pintarDiente(47,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = posx + cantsepara;
			
			pintarDiente(46,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = posx + cantsepara;
			
			pintarDiente(45,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = posx + cantsepara;
			
			pintarDiente(44,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = posx + cantsepara;
			
			pintarDiente(43,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = posx + cantsepara;
			
			pintarDiente(42,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
			posx = posx + cantsepara;
			
			pintarDiente(41,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteAdulto,Constantes.acronimoNo,4);			
																												
			//85 84 83 82 81
			
			posy = 210;
			posxLabel= 6;
			posyLabel= 15;
			
			pintarDiente(85,150,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,4);			
			posx = 150 + cantsepara;
			
			pintarDiente(84,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,4);			
			posx = posx + cantsepara;
			
			pintarDiente(83,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,4);			
			posx = posx + cantsepara;
			
			pintarDiente(82,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,4);			
			posx = posx + cantsepara;
			
			pintarDiente(81,posx,posy,tamano,cantsepara,posxLabel,posyLabel,activoDienteNino,Constantes.acronimoSi,4);			
			posx = posx + cantsepara;				
		}

		/**
		*/
		private function iniciarOdontogramaDeXML()
		{			
			for each(var dienteNodo:XML in xmlPosInicial.elements())
			{
				if(Number(dienteNodo.@pieza) > 0)
				{
					var diente:DienteOdt = this.getChildByName(dienteNodo.@pieza) as DienteOdt;
					if(!diente.getEsEvaluado)
					{
						for each(var superficieNodo:XML in dienteNodo.elements())
						{
							var modificable:Boolean=Boolean(superficieNodo.@modificable!='false');
							if(Number(superficieNodo.@codigo) > 0)
							{
								if(superficieNodo.@sector == Constantes.codigoSectorDiente1)
								{
									diente.getVestibular.setCodigo = superficieNodo.@codigo;
//									diente.getVestibular.setNombreSector = superficieNodo.@nombre;
									
									diente.getVestibular.setCodigoHallazgo = superficieNodo.hallazgo.codigo;
									diente.getVestibular.setDescripcionHallazgo = superficieNodo.hallazgo.descripcion;
									diente.getVestibular.setPathImagen = superficieNodo.hallazgo.path;
									diente.getVestibular.setCodigoConvencion = superficieNodo.hallazgo.convencion;
									diente.getVestibular.setModificable=modificable;
									diente.getVestibular.setBorde = superficieNodo.hallazgo.borde;
	
									diente.getVestibular.dibujarHallazgo(true);
								}
								
								if(superficieNodo.@sector == Constantes.codigoSectorDiente2)
								{
									diente.getMesial.setCodigo = superficieNodo.@codigo;
//									diente.getMesial.setNombreSector = superficieNodo.@nombre;
									
									diente.getMesial.setCodigoHallazgo = superficieNodo.hallazgo.codigo;
									diente.getMesial.setDescripcionHallazgo = superficieNodo.hallazgo.descripcion;
									diente.getMesial.setPathImagen = superficieNodo.hallazgo.path;
									diente.getMesial.setCodigoConvencion = superficieNodo.hallazgo.convencion;
									diente.getMesial.setModificable=modificable;
									diente.getMesial.setBorde = superficieNodo.hallazgo.borde;
	
									diente.getMesial.dibujarHallazgo(true);
								}
								
								if(superficieNodo.@sector == Constantes.codigoSectorDiente3)
								{
									diente.getLingual.setCodigo = superficieNodo.@codigo;
//									diente.getLingual.setNombreSector = superficieNodo.@nombre;
									
									diente.getLingual.setCodigoHallazgo = superficieNodo.hallazgo.codigo;
									diente.getLingual.setDescripcionHallazgo = superficieNodo.hallazgo.descripcion;
									diente.getLingual.setPathImagen = superficieNodo.hallazgo.path;
									diente.getLingual.setCodigoConvencion = superficieNodo.hallazgo.convencion;
									diente.getLingual.setModificable=modificable;
									diente.getLingual.setBorde = superficieNodo.hallazgo.borde;
	
									diente.getLingual.dibujarHallazgo(true);
								}
								
								if(superficieNodo.@sector == Constantes.codigoSectorDiente4)
								{
									diente.getDistal.setCodigo = superficieNodo.@codigo;
//									diente.getDistal.setNombreSector = superficieNodo.@nombre;
									
									diente.getDistal.setCodigoHallazgo = superficieNodo.hallazgo.codigo;
									diente.getDistal.setDescripcionHallazgo = superficieNodo.hallazgo.descripcion;
									diente.getDistal.setPathImagen = superficieNodo.hallazgo.path;
									diente.getDistal.setCodigoConvencion = superficieNodo.hallazgo.convencion;
									diente.getDistal.setModificable=modificable;
									diente.getDistal.setBorde = superficieNodo.hallazgo.borde;
									
									diente.getDistal.dibujarHallazgo(true);
								}
								
								if(superficieNodo.@sector == Constantes.codigoSectorDiente5)
								{
									diente.getOclusal.setCodigo = superficieNodo.@codigo;
//									diente.getOclusal.setNombreSector = superficieNodo.@nombre;
									
									diente.getOclusal.setCodigoHallazgo = superficieNodo.hallazgo.codigo;
									diente.getOclusal.setDescripcionHallazgo = superficieNodo.hallazgo.descripcion;
									diente.getOclusal.setPathImagen = superficieNodo.hallazgo.path;
									diente.getOclusal.setCodigoConvencion = superficieNodo.hallazgo.convencion;
									diente.getOclusal.setModificable=modificable;
									diente.getOclusal.setBorde = superficieNodo.hallazgo.borde;
									
									diente.getOclusal.dibujarHallazgo(true);
								}
								diente.setCodigoHallazgo=Constantes.codigoNuncaValido+"";
							}
							else
							{
								diente.setCodigoHallazgo = superficieNodo.hallazgo.codigo;
								diente.setDescripcionHallazgo = superficieNodo.hallazgo.descripcion;
								diente.setPathImagen = superficieNodo.hallazgo.path;							
								diente.setCodigoConvencion = superficieNodo.hallazgo.convencion;							
								diente.setModificable=modificable;
								diente.setBorde = superficieNodo.hallazgo.borde;
								diente.dibujarHallazgo(true);
							}
						}
						
						diente.setEsEvaluado = true;
					}
				}
			}
			
			xmlPosInicial = new XML();
		}

		/**
		Genera el xml del odontograma
		*/
		private function confirmarOdontogramaEvent(e:MouseEvent)
		{
			confirmarOdontograma();
		}

		/**
		Genera el xml del odontograma
		*/
		private function confirmarOdontograma()
		{
			var xml:String = "";
			for(var i:Number = 0; i < this.numChildren; i++)
			{
				if(this.getChildAt(i) is DienteOdt)
				{
					var diente:DienteOdt = this.getChildAt(i) as DienteOdt;					
					if(diente.esDienteUsado())
					{
						xml += "<diente pieza = '"+diente.getNumeroDiente+"' > ";
						
						if(Number(diente.getCodigoHallazgo) > 0)
						{
							xml += "<superficie codigo = '"+Constantes.codigoNuncaValido+"' nombre = 'diente' modificable='"+diente.getModificable+"'> "+
									   "<hallazgo>"+
									   		"<codigo>"+diente.getCodigoHallazgo+"</codigo>"+
											"<descripcion>"+diente.getDescripcionHallazgo+"</descripcion>"+
											"<path>"+diente.getPathImagen+"</path>"+
											"<convencion>"+diente.getCodigoConvencion+"</convencion>"+
											"<borde>"+diente.getBorde+"</borde>"+
									   "</hallazgo>"+
									"</superficie> ";
						}
						else
						{
							if(Number(diente.getVestibular.getCodigoHallazgo) > 0)
							{
//								xml += "<superficie codigo = '1' nombre = '"+seccion1Txt+"' sector = '"+seccion1Sec+"' > "+
								xml += "<superficie codigo = '1' nombre = '"+diente.getVestibular.getNombreSector+"' sector = '"+diente.getVestibular.getCodigoSector+"' modificable='"+diente.getVestibular.getModificable+"'> "+
										   "<hallazgo>"+
												"<codigo>"+diente.getVestibular.getCodigoHallazgo+"</codigo>"+
												"<descripcion>"+diente.getVestibular.getDescripcionHallazgo+"</descripcion>"+
												"<path>"+diente.getVestibular.getPathImagen+"</path>"+
												"<convencion>"+diente.getVestibular.getCodigoConvencion+"</convencion>"+
												"<borde>"+diente.getVestibular.getBorde+"</borde>"+
										   "</hallazgo>"+
										"</superficie> "; 
							}
							
							if(Number(diente.getMesial.getCodigoHallazgo) > 0)
							{
//								xml += "<superficie codigo = '1' nombre = '"+seccion2Txt+"' sector = '"+seccion2Sec+"' > "+
								xml += "<superficie codigo = '1' nombre = '"+diente.getMesial.getNombreSector+"' sector = '"+diente.getMesial.getCodigoSector+"' modificable='"+diente.getMesial.getModificable+"'> "+
										   "<hallazgo>"+
												"<codigo>"+diente.getMesial.getCodigoHallazgo+"</codigo>"+
												"<descripcion>"+diente.getMesial.getDescripcionHallazgo+"</descripcion>"+
												"<path>"+diente.getMesial.getPathImagen+"</path>"+
												"<convencion>"+diente.getMesial.getCodigoConvencion+"</convencion>"+
												"<borde>"+diente.getMesial.getBorde+"</borde>"+
										   "</hallazgo>"+
										"</superficie> "; 
							}
							
							if(Number(diente.getLingual.getCodigoHallazgo) > 0)
							{
//								xml += "<superficie codigo = '1' nombre = '"+seccion3Txt+"' sector = '"+seccion3Sec+"' > "+
								xml += "<superficie codigo = '1' nombre = '"+diente.getLingual.getNombreSector+"' sector = '"+diente.getLingual.getCodigoSector+"' modificable='"+diente.getLingual.getModificable+"'> "+
										   "<hallazgo>"+
												"<codigo>"+diente.getLingual.getCodigoHallazgo+"</codigo>"+
												"<descripcion>"+diente.getLingual.getDescripcionHallazgo+"</descripcion>"+
												"<path>"+diente.getLingual.getPathImagen+"</path>"+
												"<convencion>"+diente.getLingual.getCodigoConvencion+"</convencion>"+
												"<borde>"+diente.getLingual.getBorde+"</borde>"+
										   "</hallazgo>"+
										"</superficie> "; 
							}
							
							if(Number(diente.getDistal.getCodigoHallazgo) > 0)
							{
//								xml += "<superficie codigo = '1' nombre = '"+seccion4Txt+"' sector = '"+seccion4Sec+"' > "+
								xml += "<superficie codigo = '1' nombre = '"+diente.getDistal.getNombreSector+"'  sector = '"+diente.getDistal.getCodigoSector+"' modificable='"+diente.getDistal.getModificable+"'> "+
										   "<hallazgo>"+
												"<codigo>"+diente.getDistal.getCodigoHallazgo+"</codigo>"+
												"<descripcion>"+diente.getDistal.getDescripcionHallazgo+"</descripcion>"+
												"<path>"+diente.getDistal.getPathImagen+"</path>"+
												"<convencion>"+diente.getDistal.getCodigoConvencion+"</convencion>"+
												"<borde>"+diente.getDistal.getBorde+"</borde>"+
										   "</hallazgo>"+
										"</superficie> "; 
							}
							
							if(Number(diente.getOclusal.getCodigoHallazgo) > 0)
							{
								xml += "<superficie codigo = '1' nombre = '"+diente.getOclusal.getNombreSector+"' sector = '"+diente.getOclusal.getCodigoSector+"' modificable='"+diente.getOclusal.getModificable+"'> "+
										   "<hallazgo>"+
												"<codigo>"+diente.getOclusal.getCodigoHallazgo+"</codigo>"+
												"<descripcion>"+diente.getOclusal.getDescripcionHallazgo+"</descripcion>"+
												"<path>"+diente.getOclusal.getPathImagen+"</path>"+
												"<convencion>"+diente.getOclusal.getCodigoConvencion+"</convencion>"+
												"<borde>"+diente.getOclusal.getBorde+"</borde>"+
										   "</hallazgo>"+
										"</superficie> "; 
							}
						}
						
						xml += "</diente>";
					}
				}
			}
			
			if(xml!="")
			{
				xml = "<contenido>"+xml+"</contenido>";
			}
			
			String(ExternalInterface.call("outConfirmarOdontograma",xml));			
			//trace(xml);
			generarmapapixelEvento();
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
		
		public function setOdontogramaEditable(param:Boolean):void
		{
			this.odontogramaEditable=param;
		}

		public function getOdontogramaEditable():Boolean
		{
			return this.odontogramaEditable;
		}

		/**
		
		*/
		public function setHallazgosSuperficie(hallazgoSuper:String)
		{
			if(hallazgoSuper == "" || hallazgoSuper == "null" || hallazgoSuper.length <= 0 || hallazgoSuper == "undefined")
			{
				hallazgoSuper =
						"<Contenido>"+
							"<Elemento>"+
								"<codigo>"+Constantes.codigoNuncaValido+"</codigo>"+
								"<descripcion>Seleccione</descripcion>"+
								"<imagen></imagen>"+
							"</Elemento>"+
							// DATOS DE PRUEBA
/*
							"<Elemento>"+
								"<codigo>1</codigo>"+
								"<descripcion>Hola</descripcion>"+
								"<imagen>1w.jpg</imagen>"+
								"<borde>0x00FF00</borde>"+
							"</Elemento>"+
							"<Elemento>"+
								"<codigo>2</codigo>"+
								"<descripcion>Hola</descripcion>"+
								"<imagen>2.jpg</imagen>"+
								"<borde></borde>"+
							"</Elemento>"+
							"<Elemento>"+
								"<codigo>3</codigo>"+
								"<descripcion>Nada</descripcion>"+
								"<imagen></imagen>"+
								"<borde>0x00FF00</borde>"+
							"</Elemento>"+
*/
						"</Contenido>";
			}			
			var arrayHallazgosSuperXml:XML = new XML(hallazgoSuper);
			for each(var nodo:XML in arrayHallazgosSuperXml.elements())
			{
				dpHallazgosSuper.addItem( { value: nodo.codigo,label: nodo.descripcion,path:nodo.imagen+"",convencion:nodo.convencion+"", borde:nodo.borde} );
			}
		}
		
		/**
		
		*/
		public function setHallazgosDiente(hallazgoDiente:String)
		{
			//Analisis de variables externar
			if(hallazgoDiente == "" || hallazgoDiente == "null" || hallazgoDiente.length <= 0 || hallazgoDiente == "undefined")
			{
				hallazgoDiente =
						"<Contenido>"+
							"<Elemento>"+
								"<codigo>"+Constantes.codigoNuncaValido+"</codigo>"+
								"<descripcion>Seleccione</descripcion>"+
								"<imagen></imagen>"+
							"</Elemento>"+
							//DATOS DE PRUEBA -- Comentar
/*
							"<Elemento>"+
								"<codigo>1</codigo>"+
								"<descripcion>Hallazgo P</descripcion>"+
								"<imagen>3.jpg</imagen>"+
								"<borde>0x00FFF0</borde>"+
							"</Elemento>"+
							"<Elemento>"+
								"<codigo>2</codigo>"+
								"<descripcion>Hallazgo Z</descripcion>"+
								"<imagen>4.jpg</imagen>"+
								"<borde></borde>"+
							"</Elemento>"+
*/
						"</Contenido>";
			}			

			var arrayHallazgosDienteXml:XML = new XML(hallazgoDiente);
			for each(var nodo:XML in arrayHallazgosDienteXml.elements())
			{
				trace(nodo.borde);
				dpHallazgosDiente.addItem( { value: nodo.codigo,label: nodo.descripcion,path:nodo.imagen+"",convencion:nodo.convencion+"", borde:nodo.borde} );
			}
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
			Busca las imagenes de las superficies
		*/
		public function getImagenInicialArray(codigoH:Number,pathH:String):Bitmap
		{
			for(var i = 0; i < this.arrayImagenes.length; i++)
			{
				//trace(arrayImagenes[i][0]+" :: "+arrayImagenes[i][1])
				if(arrayImagenes[i][0] == codigoH 
					  && arrayImagenes[i][1] == pathH 
						  	&& arrayImagenes[i][2] is Bitmap)
				{
					var bitmapCopy:Bitmap = new Bitmap(arrayImagenes[i][2].bitmapData.clone());
					return bitmapCopy;
				}				
			}
			
			return null;
		}
		
		/**
			Actualiza el array que contiene las imagenes de hallazgos 
		*/
		public function setImagenInicialArray(codigoH:Number,pathH:String,imagen:Bitmap)
		{
			//busca el elemento en el array
			var encontro = false;
			for(var i = 0; i < this.arrayImagenes.length && !encontro; i++)
			{
				if(arrayImagenes[i][0] == codigoH 
					  && arrayImagenes[i][1] == pathH)
					encontro = true;
			}
				
			if(!encontro && codigoH > 0 && pathH != "" && imagen!=null)
			{
				//trace("ingreso >> "+codigoH,pathH);
				var array = new Array(codigoH,pathH,imagen);
				arrayImagenes.push(array);
			}				
		}
		
		
	
		/**
		Genera el contenido del Movie Clip en un mapa de pixels		
		*/
		function generarmapapixelEvento():Boolean
		{
			try
			{
				var jpgSource:BitmapData = new BitmapData (700,325,true);
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
				
				//ExternalInterface.call("outErrorSwf",this.rutaImagen);
				
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
		
		function onGuardarImagen(e:Event):void
		{
			try{
				// Cerrar la ventana
				ExternalInterface.call("window.parent.cerrarVentanaOdontoDx");
			}
			catch(errIgnorar:Error)
			{
				ExternalInterface.call("outErrorSwf",errIgnorar.toString()+" "+errIgnorar.message+" "+errIgnorar.name);
				// En caso de que sea funcionalidad plana no va a existir esta función
				// Por lo tanto simplemente ignoro el error
			}
		}

		
		/**
		Actualiza la informaciòn para la generaciòn de la imagen generada
		*/
		public function setInfoImagen(ruta:String)
		{
			rutaImagen = ruta;
		}
		
		public function get getDpHallazgosSuper():DataProvider
		{
			return this.dpHallazgosSuper;
		}
		public function get getDpHallazgosDiente():DataProvider
		{
			return this.dpHallazgosDiente;
		}
		
		public static function obtenerDataProviderXCodigo(proveedorDatos:DataProvider, codigo:String):String
		{
			for(var i:int; i<proveedorDatos.length; i++)
			{
				var item:Object=proveedorDatos.getItemAt(i);
				//trace("item "+item.value);
				if(item.value==codigo)
				{
					return item.label;
				}
			}
			return "";
		}
	}	
}