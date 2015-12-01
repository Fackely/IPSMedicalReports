package mundo.Odontograma
{
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.geom.ColorTransform;
	import fl.controls.CheckBox;
	import flash.display.LineScaleMode;
	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.display.DisplayObject;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Shape;
	import flash.net.URLRequest;
	import flash.geom.Matrix;
	import flash.display.BlendMode;

	import util.general.Constantes;
	
	public class SuperficieDienteOdt extends Sprite
	{
		//
		private var codigo:String;
		
		//Indica el sector del diente
		private var nombreSector:String;
		
		//Indica el codigo del Sector del diente
		private var codigoSector:int;
		
		//Indica si fue seleccionado
		private var marcado:String;
		
		//
		private var codigoHallazgo:String;
		
		//
		private var descripcionHallazgo:String;
		
		//
		private var pathImagen:String;
		
		// Borde de la imagen
		private var borde:String;

		//
		private var bitmap:Bitmap;
		
		//
		private var cargarImagenArray:Boolean;
		
		//
		private var codigoConvencion:String;
		
		//Contenedor de la imagen
		var ct_imagen:Shape = new Shape();

		private var modificable:Boolean;

		/**
		*/
		public function SuperficieDienteOdt(
										param_codigo:String,
										param_nombreSector:String,
										param_codigosector:int,
										param_marcado:String):void
		{
			//Oclusal
			if(param_codigosector == Constantes.codigoSectorDiente5)
			{
				removerObjetos();
				var oclusal:Sprite = new Sprite();
				oclusal.graphics.beginFill(0xFFFFFF);
				oclusal.graphics.drawCircle(0,0,13);
				oclusal.graphics.endFill();
				this.addChild(oclusal);
			}

			this.nombreSector = param_nombreSector;
			this.marcado = param_marcado;
			this.useHandCursor = true;
			this.codigo = param_codigo;
			this.codigoSector = param_codigosector;
			this.bitmap = new Bitmap();
			this.cargarImagenArray = false;
			this.codigoConvencion = "";
			this.modificable=true;
			this.codigoHallazgo=Constantes.codigoNuncaValido+"";
		}
		
		/**
		*/
		public function removerObjetos()
		{
			//Libera los objetos contenidos
			var l:int = this.numChildren-1;
			var j:int = 0;
			for(j = l; j >= 0;j--){
				this.removeChildAt(j);
			}
		}
		
		/**
		Actualiza la informacion de la superficie
		*/
		public function actualizarSuperficie(codigo:String,nombre:String,path:String,convencion:String,borde:String)
		{
			this.descripcionHallazgo = nombre;
			this.codigoHallazgo = codigo;
			this.pathImagen = path;
			this.borde = borde;
			this.codigoConvencion = convencion;
			ct_imagen.graphics.clear();
			
			this.graphics.clear();
			
			if(Number(this.codigoHallazgo) > 0 && this.pathImagen!="")
			{
				getImagenHallazgo();
			}
			else
			{
				this.pathImagen="";
				//Libera la memoria del bitmap cargado
				if(this.bitmap.bitmapData!=null)
				{
					this.bitmap.bitmapData.dispose();
					this.bitmap.bitmapData = null;
				}
			}
			
		}
		
		/**
		Dibuja el hallazgo
		*/
		public function dibujarHallazgo(cargo_param:Boolean)
		{
			cargarImagenArray = cargo_param;
			if(cargarImagenArray)						
			{
				getImagenHallazgo();
			}
			else
			{
				actualizarCtImagen();
			}
		}

		
		/**
		Obtiene la imagen del hallazgo	
		*/
		public function getImagenHallazgo()
		{
			if(this.pathImagen!=null && this.pathImagen!="")
			{
				var loader:Loader;
				loader = new Loader();
				loader.contentLoaderInfo.addEventListener(Event.COMPLETE,completeImagen);
				loader.load(new URLRequest(this.pathImagen));
			}
		}
		
		/**		
		*/
		public function completeImagen(event:Event)
		{			
			var loaderImg:Loader = Loader(event.target.loader);
			bitmap = Bitmap(loaderImg.content);
			var tamanio:int=150;

			var bitmapDataOriginal:BitmapData=bitmap.bitmapData;
			var scaleFactor:Number=30/tamanio;
			var newWidth:Number=bitmapDataOriginal.width*scaleFactor;
			var newHeight:Number=bitmapDataOriginal.height*scaleFactor;
//			trace(bitmap.x+" "+bitmap.y+" "+bitmap.scaleY+" "+bitmap.scaleX+" "+bitmapDataOriginal.height+" "+bitmapDataOriginal.width);
			var bitmapDataEscalado:BitmapData=new BitmapData(newWidth,newHeight,true,0xFFFFFFFF);
			var matrizEscalar:Matrix=new Matrix();
			matrizEscalar.scale(scaleFactor,scaleFactor);
			var translacion=-this.rotation;
			matrizEscalar.rotate(translacion*Math.PI/180);
			switch(translacion)
			{
				case -90:
					matrizEscalar.translate(0, 30);
				break;
				case -180:
					matrizEscalar.translate(30, 30);
				break;
				case 90:
					matrizEscalar.translate(30, 0);
				break;
			}
			bitmapDataEscalado.draw(bitmapDataOriginal,matrizEscalar);
			bitmap.bitmapData=bitmapDataEscalado;

//			trace(bitmap.x+" "+bitmap.y+" "+bitmap.scaleY+" "+bitmap.scaleX+" "+bitmap.bitmapData.height+" "+bitmap.bitmapData.width);
			actualizarCtImagen();
			loaderImg.unload();
		}
		
		/**		
		*/
		private function actualizarCtImagen()
		{
			if(bitmap!=null && bitmap.bitmapData!=null)
			{
				var transladar:Matrix=new Matrix();
				transladar.translate(19,-10);
				ct_imagen.graphics.clear();
				ct_imagen.graphics.beginBitmapFill(bitmap.bitmapData,transladar,false,true);
				var desp:int=0;
				
				if(borde!=null && borde!='')
				{
					ct_imagen.graphics.lineStyle(3, uint(borde));
				}
				else
				{
					desp = 3;
				}
				
				if(codigoSector == Constantes.codigoSectorDiente5)
				{
					ct_imagen.graphics.drawCircle(34,5.5,11+desp);
				}
				else
				{
					// Izq
					ct_imagen.graphics.moveTo(3-desp,0);
					ct_imagen.graphics.lineTo(24-desp,21+desp);
					// Inf
					ct_imagen.graphics.curveTo(33.5,15+desp,43+desp,21+desp); // (curvaX, curvaY, finalX, finalY);
					// Der
					ct_imagen.graphics.lineTo(63.5+desp,0.5);
					//Sup Der
					ct_imagen.graphics.curveTo(51.25,-11-desp,33.5,-11-desp);
					//Sup Izq
					ct_imagen.graphics.curveTo(17.75,-11.5-desp,3-desp,0-desp);
					
				}
				
				ct_imagen.graphics.endFill();
				ct_imagen.x = -34;
				ct_imagen.y = -5;
	
				ct_imagen.name="imagenHallazgo";
				var spriteEliminar:Shape = Shape(this.getChildByName("imagenHallazgo"));
				if(spriteEliminar==null)
				{
					this.addChildAt(ct_imagen,1);
				}
			}
		}
		
		public function get getNombreSector():String
		{
			return this.nombreSector;
		}
		
		public function set setNombreSector(param_value:String):void
		{
			this.nombreSector = param_value;
		}
		
		public function get getCodigoSector():int
		{
			return this.codigoSector;
		}
		
		public function set setCodigoSector(param_value:int):void
		{
			this.codigoSector = param_value;
		}
		
		public function get getMarcado():String
		{
			return this.marcado;
		}
		
		public function set getMarcado(param_value:String):void
		{
			this.marcado = param_value;
		}
		
		public function get getCodigoHallazgo():String
		{
			return this.codigoHallazgo;
		}
		
		public function set setCodigoHallazgo(param_value:String):void
		{
			this.codigoHallazgo = param_value;
		}
		
		public function get getDescripcionHallazgo():String
		{
			return this.descripcionHallazgo;
		}
		
		public function set setDescripcionHallazgo(param_value:String):void
		{
			this.descripcionHallazgo = param_value;
		}
		
		public function get getPathImagen():String
		{
			return this.pathImagen;
		}
		
		public function set setPathImagen(param_value:String):void
		{
			this.pathImagen = param_value;
		}
		
		public function get getCodigo():String
		{
			return this.codigo;
		}
		
		public function set setCodigo(param_value:String):void
		{
			this.codigo = param_value;
		}		
		
		public function get getBitmap():Bitmap
		{
			return this.bitmap;
		}
		
		public function set setBitmap(param_value:Bitmap):void
		{
			this.bitmap = param_value;
		}
		
		public function get getCodigoConvencion():String
		{
			return this.codigoConvencion;
		}
		
		public function set setCodigoConvencion(param_value:String):void
		{
			this.codigoConvencion = param_value;
		}
		
		public function get getModificable():Boolean
		{
			return this.modificable;
		}
		
		public function set setModificable(modificable:Boolean):void
		{
			this.modificable=modificable;
		}
		
		public function get getBorde():String
		{
			return this.borde;
		}

		public function set setBorde(borde:String):void
		{
			this.borde = borde;
		}

	}
}