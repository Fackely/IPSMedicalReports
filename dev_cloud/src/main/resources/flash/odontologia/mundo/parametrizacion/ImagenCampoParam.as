package mundo.parametrizacion
{
	import flash.display.MovieClip;
	import flash.display.Sprite;
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.events.*;
	import flash.external.ExternalInterface;
	import flash.display.Loader;
	import flash.net.URLRequest;
	import flash.display.DisplayObject;
	import flash.display.SimpleButton;	
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.text.AntiAliasType;
	import flash.utils.ByteArray;
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;	
	import flash.net.navigateToURL;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	
	import util.corel.src.com.adobe.images.JPGEncoder;
	import util.corel.src.com.adobe.UploadPostHelper;
	import util.corel.src.com.adobe.MultipartURLLoader;
	import mundo.parametrizacion.ImagenRelleno;
	
	public class ImagenCampoParam extends MovieClip
	{
		var mcImagen:MovieClip;
		var rutaImagenBase:String = "";
		var rutaImagenRelleno:String = "";
		var indicadorOpcion:String = "";
		var contImagenRelleno:int = 0;
		var bitmap:Bitmap;
		var largoEscenario:int = 381;
		var anchoEscenario:int = 730;
		
		var largoImagen:int = 320;
		var anchoImagen:int = 700;
		
		var valorZoom:int = 0;
		var formaRelleno = "cuadrado";
		
		var labelMensajes:TextField;
		var formatMensajes:TextFormat;
		var xmlOpcion:String  = "";

		var urlGeneracionImagen:String="";
		
		var pkCampo:String=null;
		var debug:String="false";
		
		function loaderComplete(myEvent:Event)
		{
			var parametros=this.loaderInfo.parameters;
			pkCampo=parametros.pkCampo;
			debug=parametros.debug;
			ExternalInterface.call("loadFlashImagenBase", pkCampo);
		}
		// Initialization:
		public function ImagenCampoParam()
		{			
			this.loaderInfo.addEventListener(Event.COMPLETE, loaderComplete);
			labelMensajes =  new TextField();
			formatMensajes = new TextFormat();
			formatMensajes.color = 0x000000;
            formatMensajes.size = 15;
            formatMensajes.bold = true;
            formatMensajes.italic = false;						
			labelMensajes.defaultTextFormat = formatMensajes;
			
			mcImagen = new MovieClip();	
			
			//Añade la funcion a la interfaz
			ExternalInterface.addCallback("setswfrutaimgbase",setswfrutaimgbase);
			ExternalInterface.addCallback("setswfrutaimgrelleno",setswfrutaimgrelleno);
			ExternalInterface.addCallback("setswfindicadoropcion",setswfindicadoropcion);
			ExternalInterface.addCallback("setswfanchoaltoimg",setswfanchoaltoimg);
			ExternalInterface.addCallback("swfpintar",swfpintar);
			ExternalInterface.addCallback("swfespera",lanzarPanelEspera);			
			ExternalInterface.addCallback("swfquitarPanelEspera",quitarPanelEspera);
			ExternalInterface.addCallback("setURL",setURL);
			
			/*
			DATOS DE PRUEBA
			rutaImagenBase = "imagenbase.gif";
			rutaImagenRelleno = "imagenrelleno.gif";			
			swfpintar();*/
		}	
		
		
		/*
		*/
		public function swfpintar()
		{
			removerElementos();
			if (ExternalInterface.available) {
                try 
				{
					xmlOpcion = String(ExternalInterface.call("getxmlopcion",indicadorOpcion));
					
					if(xmlOpcion== "null" || xmlOpcion== "" || xmlOpcion == "undefined")
					{
						xmlOpcion = "";
					}
				}
				catch (error:Error) 
				{					
					xmlOpcion = "";					
				}
			}
			
			adicionarImagenes();
		}		
		
		/**		
		*/
		public function adicionarVariablesExternas()
		{
			 if (ExternalInterface.available) {
                try 
				{
					xmlOpcion = String(ExternalInterface.call("getxmlopcion",indicadorOpcion));
					rutaImagenBase = String(ExternalInterface.call("getrutaimgbase"));
					rutaImagenRelleno = String(ExternalInterface.call("getrutaimgrelleno"));
					indicadorOpcion = String(ExternalInterface.call("getindicadoropcion"));
													
					if(xmlOpcion== "null" || xmlOpcion== "" || xmlOpcion == "undefined")
						xmlOpcion = "";
													
					if(indicadorOpcion== "null" || indicadorOpcion== "" || indicadorOpcion == "undefined")
						indicadorOpcion = "";				
						
					if(rutaImagenBase== "null" || rutaImagenBase== "" || rutaImagenBase == "undefined")
						rutaImagenBase = "";				
						
					if(rutaImagenRelleno== "null" || rutaImagenRelleno== "" || rutaImagenRelleno == "undefined")
						rutaImagenRelleno = "";					
					
				} catch (error:Error) 
				{
					indicadorOpcion = "";
					rutaImagenBase = "";
					rutaImagenRelleno = "";
					xmlOpcion = "";					
				}
			 }
		}
		
		/**
		*/
		public function adicionarImagenes()
		{			
			if(rutaImagenBase!="" && rutaImagenBase!="null")
			{
				var loader:Loader;
				loader = new Loader();
				loader.contentLoaderInfo.addEventListener(Event.COMPLETE,completeImagenBase);				
				loader.load(new URLRequest(rutaImagenBase));			
			}
			else
			{				
				lanzarPanelEspera();
				
				labelMensajes.text ="No se Encuentran Imagenes Base/Opción";
				labelMensajes.x = (317.5 /2) - 250;
				labelMensajes.y = (195.9/2) - 125;
				labelMensajes.autoSize = TextFieldAutoSize.LEFT;
				labelMensajes.antiAliasType = AntiAliasType.NORMAL; 
				
				this.mc_espera.addChild(labelMensajes);
			}
		}
		
		/**		
		*/
		private function completeImagenBase(event:Event)
		{
		 	var loader:Loader = Loader(event.target.loader);
			var sobrepaso:Boolean = false;

			if(loader.width > anchoImagen)
				sobrepaso = true;
			else
				anchoImagen = loader.width;
				
			if(loader.height > largoImagen)
				sobrepaso = true;
			else
				largoImagen = loader.height;
				
			if(sobrepaso)
			{
				lanzarPanelEspera();
				
				labelMensajes.text ="La Imagen Base supera el Maximo Tamaño Permitido ("+anchoImagen+" x "+largoImagen+").";
				labelMensajes.x = (317.5 /2) - 310;
				labelMensajes.y = (195.9/2) - 125;
				labelMensajes.width = 20;
				labelMensajes.height = 1;
				labelMensajes.autoSize = TextFieldAutoSize.LEFT;
				labelMensajes.antiAliasType = AntiAliasType.NORMAL; 

				this.mc_espera.addChild(labelMensajes);
			}
			else
			{
				mcImagen.addChild(loader);

				var loaderImgRelleno:Loader;
				loaderImgRelleno = new Loader();
				loaderImgRelleno.contentLoaderInfo.addEventListener(Event.COMPLETE,completeImagenRelleno);
				loaderImgRelleno.load(new URLRequest(rutaImagenRelleno));
			}
		}
						
		/**		
		*/
		private function completeImagenRelleno(event:Event)
		{
			var loaderImgRelleno:Loader = Loader(event.target.loader);
			bitmap = Bitmap(loaderImgRelleno.content);
			
			mcImagen.addEventListener(MouseEvent.CLICK,relleno);

			mcImagen.x = (anchoEscenario/2) - (mcImagen.width/2) ;
			//se resta 18.5 es el tamaño de los botones y marcos
			mcImagen.y = (largoEscenario/2) - (mcImagen.height/2) - 18.5;
			
			//Adiciona las acciones de los botones
			this.btn_zoomas.addEventListener(MouseEvent.CLICK,zoomMas);
			this.btn_zoomenos.addEventListener(MouseEvent.CLICK,zoomMenos);
			this.btn_cuadrado.addEventListener(MouseEvent.CLICK,setFormaCuadrado);
			this.btn_redondo.addEventListener(MouseEvent.CLICK,setFormaRedondo);
			this.btn_confirmar.addEventListener(MouseEvent.CLICK,exportarDatosImagen);
			
			this.addChild(mcImagen);
			
			//carga la informacion de los elementos si encuentra XML
			cargarIndicePlacaXML();
		}
		
		/**		
		*/
		public function relleno(evento:Event)
		{
			if(!(evento.target is ImagenRelleno))
			{
				var bitmapCopy:Bitmap = new Bitmap(bitmap.bitmapData.clone());
				var imgRelleno:ImagenRelleno = new ImagenRelleno(formaRelleno,bitmapCopy);
				
				imgRelleno.width = bitmapCopy.width + valorZoom;
				imgRelleno.height = bitmapCopy.height + valorZoom;

				imgRelleno.x = ((evento.target.mouseX) - (imgRelleno.width/2));
				imgRelleno.y = ((evento.target.mouseY) - (imgRelleno.height/2));

				mcImagen.addChild(imgRelleno);
			}
		}
		
		/**
		
		*/
		private function lanzarPanelEspera()
		{
			this.mc_espera.width = anchoEscenario ;
			this.mc_espera.height = largoEscenario;
			
			this.mc_espera.x = 317.5;
			this.mc_espera.y = 195.9;
			
			addChild(this.mc_espera);
		}
		
		/**
		
		*/
		private function quitarPanelEspera()
		{			
			addChild(this.mc_espera);
		}
		
		
		/**
		
		*/
		private function exportarDatosImagen(event:Event)
		{
			generarimagenJpg();
			
			//genera el XML con las posciones de los elementos de relleno
			generarXML();
		}
		
		/**
		Genera el contenido del Movie Clip en un mapa de pixels		
		*
		public function generarmapapixel():void
		{
			ExternalInterface.call("outErrorSwf","Si entra generarmapapixel");
			lanzarPanelEspera();
			var bmp:BitmapData = new BitmapData(anchoImagen,largoImagen,true);
			bmp.draw(mcImagen);	
				
			var output:String = "";
			var col = "";
			for(var i:Number=0;i<bmp.height;i++)
			{
				for(var j:Number=0;j<bmp.width;j++)
				{			
					col = bmp.getPixel(j,i).toString()+",";       
			
					// In some cases, the color will be truncated (e.g. "00FF00" becomes "FF00")
					// so we are adding the missing zeros.
					while(col.length<6)
					{
						col = "0" + col;
					}
					output+=col;
				}
			}
			
			//trace(output)
			//Envia el mapa de pixeles
			String(ExternalInterface.call("setmapapixelopcion",output,indicadorOpcion));
			removeChild(this.mc_espera);
		}*/
		
		/**
		
		*/
		public function generarimagenJpg()
		{
			if(this.debug=="true")
			{
				ExternalInterface.call("outErrorSwf",this.urlGeneracionImagen);
			}
			var jpgSource:BitmapData = new BitmapData (anchoImagen,largoImagen,true);
			jpgSource.draw(mcImagen);
			
			var jpgEncoder:JPGEncoder = new JPGEncoder(85);
			var jpgStream:ByteArray = jpgEncoder.encode(jpgSource);
			
			var req:MultipartURLLoader = new MultipartURLLoader();
			req.addFile(jpgStream,'imagenBase', 'file');
			req.load(this.urlGeneracionImagen+"?idImagen="+);
			jpgStream.clear();
			jpgSource.dispose();
/*
			if(aux=="1")
			{
				//*****
				var header:URLRequestHeader = new URLRequestHeader("Content-type","application/octet-stream");			
				var jpgURLRequest:URLRequest = new URLRequest("generarimagenopcion.jsp?idimagen=sketch.jpg");			
				jpgURLRequest.requestHeaders.push(header);				
				jpgURLRequest.method = URLRequestMethod.POST;
				jpgURLRequest.data = jpgStream;
				navigateToURL(jpgURLRequest, "_blank");
				//*******
			}
				
			if(aux=="2")
			{	
				var jpgURLRequest1 : URLRequest = new URLRequest();	
				jpgURLRequest1.url = "generarimagenopcion.jsp?idimagen=sketch.jpg";
				jpgURLRequest1.contentType='multipart/form-data; boundary='+UploadPostHelper.getBoundary();
				jpgURLRequest1.method=URLRequestMethod.POST;			
				jpgURLRequest1.data=UploadPostHelper.getPostData("test",jpgStream);
				jpgURLRequest1.requestHeaders.push(new URLRequestHeader('Cache-Control','no-cache'));
				//cargador de la imagen que envía al server
				var urlLoader:URLLoader = new URLLoader();
				urlLoader.dataFormat=URLLoaderDataFormat.TEXT;
				urlLoader.load(jpgURLRequest1);		
			}
			
			//*****************************
			
			if(aux=="3")
			{
				var req:MultipartURLLoader = new MultipartURLLoader();
				req.addVariable('test',UploadPostHelper.getPostData("prueba",jpgStream));
				req.addVariable('test1',"llega");
				req.addFile(jpgStream, 'test1.txt', 'Filedata[]');
				req.addFile(jpgStream, 'test2.txt', 'Filedata[]', 'text/plain');	
				req.load("generarimagenopcion.jsp?idimagen=sketch.jpg");
			}			
			
			if(aux=="4")
			{	
				var jpgURLRequest2 : URLRequest = new URLRequest();	
				jpgURLRequest2.url = "generarimagenopcion.jsp?idimagen=sketch.jpg";
				jpgURLRequest2.contentType='multipart/form-data; boundary='+UploadPostHelper.getBoundary();
				jpgURLRequest2.method=URLRequestMethod.POST;			
				jpgURLRequest2.data=UploadPostHelper.getPostData("prueba",jpgStream) ;
				jpgURLRequest2.requestHeaders.push(new URLRequestHeader('Cache-Control','no-cache'));
				
				//cargador de la imagen que envía al server
				var urlLoader1:URLLoader = new URLLoader();
				urlLoader1.dataFormat = URLLoaderDataFormat.BINARY
				urlLoader1.load(jpgURLRequest2);		
			}
			
			if(aux=="5")
			{	
				//*****
				var header3:URLRequestHeader = new URLRequestHeader("Content-type","application/octet-stream");			
				var jpgURLRequest3:URLRequest = new URLRequest("generarimagenopcion.jsp?idimagen=sketch.jpg");			
				jpgURLRequest3.requestHeaders.push(header3);				
				jpgURLRequest3.method = URLRequestMethod.POST;
				jpgURLRequest3.data = jpgStream;				
				var urlLoader2:URLLoader = new URLLoader();				
				urlLoader2.load(jpgURLRequest3);
			}
			*/
			
		}
		
		/**
		Genera el xml con los estados de los elementos
		*/
		public function generarXML():void
		{
			var cadena:String = "";
			var index:Number = 0;
			var parentObjP:Object = this as Object;			

			var numElementos:int = this.mcImagen.numChildren;
			
			for(var i:Number=0; i<numElementos; i++)
			{
				if(this.mcImagen.getChildAt(i) is ImagenRelleno)
				{
					cadena += '<Elemento '+
							  'xpos= "'+mcImagen.getChildAt(i).x+'" '+  
							  'ypos= "'+mcImagen.getChildAt(i).y+'" '+
							  'ancho = "'+mcImagen.getChildAt(i).width+'" '+
							  'largo = "'+mcImagen.getChildAt(i).height+'" '+							
							  'forma = "'+parentObjP.mcImagen.getChildAt(i).getForma+'" '+
							  '></Elemento>';
				}
			}
			
			if(cadena != "")
				cadena = '<Rellenos>'+cadena+'</Rellenos>';				
			
			//Envia el contenido xml
			String(ExternalInterface.call("setxmlopcion",cadena,indicadorOpcion));			
		}
		
		/**
		Carga los elementos a partir de un xml
		*/		
		public function cargarIndicePlacaXML():void
		{
			if(xmlOpcion != "")
			{
				var arrayXml:XML = new XML(xmlOpcion);
				var parentObjP:Object = this as Object;	
				for each(var nodo:XML in arrayXml.elements())
				{
					var bitmapCopy:Bitmap = new Bitmap(bitmap.bitmapData.clone());
					var imgRelleno:ImagenRelleno = new ImagenRelleno(nodo.@forma,bitmapCopy);
				
					imgRelleno.width = nodo.@ancho;
					imgRelleno.height = nodo.@largo;

					imgRelleno.x = nodo.@xpos;
					imgRelleno.y = nodo.@ypos;

					mcImagen.addChild(imgRelleno);
				}
			}
		}
		
		/*	
		*/
		public function zoomMas(evento:Event)
		{
			if((bitmap.bitmapData.width + (valorZoom + 15)) <= (largoImagen))
				valorZoom = valorZoom + 15;
		}
		
		/*		
		*/
		public function zoomMenos(evento:Event)
		{
			if((bitmap.bitmapData.width + (valorZoom - 15)) > 0)
				valorZoom = valorZoom - 15;
		}
		
		/*		
		*/
		public function setFormaCuadrado(evento:Event)
		{
			formaRelleno = "cuadrado";			
		}
		
		/*		
		*/
		public function setFormaRedondo(evento:Event)
		{
			formaRelleno = "redondo";
		}
		
		/**
		
		*/
		public function setswfrutaimgbase(valor:String)
		{
			if(this.debug=="true")
			{
				ExternalInterface.call("outErrorSwf",valor);
			}
			this.rutaImagenBase = valor;
		}
		
		/**
		
		*/
		public function setswfrutaimgrelleno(valor:String)
		{
			this.rutaImagenRelleno = valor;
		}
		
		/**
		
		*/
		public function setswfindicadoropcion(valor:String)
		{
			this.indicadorOpcion = valor;
		}
		
		/**
		*/
		public function setswfanchoaltoimg(ancho:int,largo:int)
		{
			this.largoImagen = largo;
			this.anchoImagen = ancho;			
		}		
		
		/**
		Remueve los elementos del movie clip
		*/
		public function removerElementos()
		{
			for(var i = 0; i < this.mcImagen.numChildren; i++)
				this.mcImagen.removeChildAt(i);
		}
		
		/**
		 * Asigna el URL al cual será enviada la imagen generada al darle clic en el bot&oacute;n confirmar
		 * @autor Juan David Ram&iacute;rez
		 */
		public function setURL(urlGeneracionImagen:String)
		{
			this.urlGeneracionImagen=urlGeneracionImagen;
		}

		/**
		 * Obtiene el URL al cual será enviada la imagen generada al darle clic en el bot&oacute;n confirmar
		 * @return {@link String} URL
		 * @autor Juan David Ram&iacute;rez
		 */
		public function getURL()
		{
			return this.urlGeneracionImagen;
		}

	}
}