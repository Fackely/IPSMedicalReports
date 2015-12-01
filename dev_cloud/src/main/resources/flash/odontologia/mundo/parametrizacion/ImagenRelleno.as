package mundo.parametrizacion 
{
	import flash.display.Bitmap;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.display.DisplayObject;
	
	public class ImagenRelleno extends Sprite {
		
		// Constants:
		// Public Properties:		
		
		// Private Properties:
		private var forma:String = "";
			
		// Initialization:
		public function ImagenRelleno(v_forma:String,v_bitmap:Bitmap) 
		{ 			
			forma = v_forma;			
			addChild(v_bitmap);
						
			if(forma == "redondo")
			{
				//Mascara del contenedor
				var square:Sprite = new Sprite();
				square.graphics.beginFill(0xFFFFFF);
				square.graphics.drawCircle((this.width/2),(this.height/2),Math.floor(this.height/2));
				square.graphics.endFill();
				
				//Adjudica la mascara
				this.mask = square;
				this.addChild(square);					
			}
			
			this.useHandCursor = true;
			this.addEventListener(MouseEvent.CLICK,eliminar);			
		}		
		
		/**
		Elimina el Objeto de Relleno
		*/
		public function eliminar(e:Event)
		{			
			var padre:Object = this.parent as Object;
			padre.removeChild(DisplayObject(e.target)); 
		}
	
		// Public Methods:
		public function get getForma():String
		{
			return this.forma;
		}
		
		public function set setForma(valor:String)
		{
			this.forma = valor;
		}
		// Protected Methods:
	}	
}