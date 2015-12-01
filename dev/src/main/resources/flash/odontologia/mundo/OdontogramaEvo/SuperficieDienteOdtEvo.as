package mundo.OdontogramaEvo
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
	
	public class SuperficieDienteOdtEvo extends Sprite
	{
		//
		private var codigo:String;
		
		//Indica el sector del diente
		private var nombreSector:String;
		
		//Indica el codigo del Sector del diente
		private var codigoSector:int;
		
		//
		private var pathImagen:String;
		
		//
		private var bitmap:Bitmap;
		
		//
		private var colorS:String;
		
		//Contenedor de la imagen
		var ct_imagen:Shape = new Shape();
		
		/**
		*/
		public function SuperficieDienteOdtEvo(
										param_codigo:String,
										param_nombreSector:String,
										param_codigosector:int):void
		{
			//Oclusal
			if(param_codigosector == Constantes.codigoSectorDiente5)
			{
				removerObjetos();
				var oclusal:Sprite = new Sprite();
				oclusal.graphics.beginFill(uint("0xFFFFFF"));
				oclusal.graphics.drawCircle(0,0,15);
				oclusal.graphics.endFill();
				this.addChild(oclusal);
			}

			this.nombreSector = param_nombreSector;
//			this.useHandCursor = true;
			this.codigo = param_codigo;
			this.codigoSector = param_codigosector;
			this.bitmap = new Bitmap();
		}
		
		/**
		*/
		public function removerObjetos()
		{
			//Libera los objetos contenidos
			var l:int = this.numChildren-1
			var j:int = 0;
			for(j = l; j >= 0;j--){
				this.removeChildAt(j);
			}
		}
		
		/**
		Dibuja el hallazgo
		*/
		public function dibujarHallazgo(cargo_param:Boolean)
		{
			if(cargo_param && (colorS!= "" || pathImagen!= ""))
			{
				actualizarSuperficie(this.pathImagen,this.colorS)
			}
			else
			{
				actualizarCtImagen();
			}
		}
		
		/**
		Actualiza la informacion de la superficie
		*/
		public function actualizarSuperficie(path:String,colorParam:String)
		{
			var testClipTransform:ColorTransform;		
			this.pathImagen = path;
			this.colorS = colorParam;
			
			if(colorS!= "" || pathImagen!= "")
			{
				if(colorS!= "")
				{
					testClipTransform=new ColorTransform(0,0,0,1,190,190,190,0);
					this.transform.colorTransform = testClipTransform;
				}
				else if(pathImagen!= "")
				{
					getImagenHallazgo();
				}
			}
			else
			{
				testClipTransform=new ColorTransform(0,0,0,1,255,255,255,0);
				this.transform.colorTransform = testClipTransform;
				
				//Libera la memoria del bitmap cargado
				if(this.bitmap.bitmapData!=null)
				{
					this.bitmap.bitmapData.dispose();
					this.bitmap.bitmapData = null;
				}
			}
		}
		
		/**
		Obtiene la imagen del hallazgo	
		*/
		public function getImagenHallazgo()
		{
			var loader:Loader;
			loader = new Loader();
			loader.contentLoaderInfo.addEventListener(Event.COMPLETE,completeImagen);				
			loader.load(new URLRequest(this.pathImagen));	
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
				
				if(codigoSector == Constantes.codigoSectorDiente5)
					ct_imagen.graphics.drawCircle(34,5.5,14.5);
				else
				{
	//				ct_imagen.graphics.drawRect(19, -10, 30, 30);
					ct_imagen.graphics.moveTo(0,0);
					ct_imagen.graphics.lineTo(24,23);
					ct_imagen.graphics.curveTo(34,18,44,23); // (curvaX, curvaY, finalX, finalY);
					ct_imagen.graphics.lineTo(68,0);
					ct_imagen.graphics.curveTo(33.5,-18,0,0);
					
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
		
		public function get getColorS():String
		{
			return this.colorS;
		}
		
		public function set setColorS(param_value:String):void
		{
			this.colorS = param_value;
		}
	}
}