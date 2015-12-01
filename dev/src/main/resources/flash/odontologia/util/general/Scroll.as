package util.general 
{	
	import flash.display.*;
	import flash.events.*;
	import flash.geom.Rectangle;
	public class Scroll extends Sprite
	{
		var alto:Number;
		var separacion:Number = 10;
		var ancho:Number;
		var recorrido:Number;
		var ct_principal:Sprite;
		var ct_segundario:Sprite;
		var addPos:Number;
		var direccion:String;
		
		//Función constructora
		public function Scroll(
							   contenedor1:Sprite,
							   contenedor2:Sprite,
							   posx:Number,
							   posy:Number,
							   addPosParam:Number,
							   anc:Number,
							   alt:Number,
							   color1:uint,
							   color2:uint,
							   color3,
							   direccionParam:String):void 
		{
			ct_principal = contenedor1;
			ct_segundario = contenedor2;
			alto = alt;
			ancho = anc;			
			addPos = addPosParam;
			direccion = direccionParam;
			
			if(direccionParam == "ver")
			{			
				x = posx;
				y = posy + addPosParam;
				recorrido = 7*alto/8;
				
				CrearBarra("Barra_mc_"+direccion,ancho,alto,color1,color3);
				CrearBarra("Barrita_mc_"+direccion,ancho,alto/8,color2,color3);			
			}
			else
			{
				x = posx +addPosParam;
				y = posy;
				recorrido = 7*ancho/8;
				
				CrearBarra("Barra_mc_"+direccion,ancho,alto,color1,color3);
				CrearBarra("Barrita_mc_"+direccion,ancho/8,alto,color2,color3);			
			}
			
			getChildByName("Barrita_mc_"+direccion).addEventListener(MouseEvent.MOUSE_DOWN,Arrastrar);			
			getChildByName("Barrita_mc_"+direccion).addEventListener(MouseEvent.ROLL_OVER,Arrastrar);
			getChildByName("Barrita_mc_"+direccion).addEventListener(MouseEvent.MOUSE_UP,Soltar);
			getChildByName("Barrita_mc_"+direccion).addEventListener(MouseEvent.MOUSE_OUT,Soltar);
			
			addEventListener(Event.ENTER_FRAME,Colocar);

		}
		//Crea una barra y la añade al escenario
		private function CrearBarra(nombre:String,ancho:Number,alto:Number,color:uint,colormarco:uint):void {
			var hijo:Sprite = new Sprite();
			hijo.name = nombre;
			hijo.graphics.beginFill(color);
			hijo.graphics.lineStyle(0.1, colormarco);
			hijo.graphics.drawRect(0, 0, ancho, alto);
			hijo.alpha = 0.5;
			hijo.useHandCursor = true;
			hijo.buttonMode = true;			
			hijo.graphics.endFill();
			addChild(hijo);
		}
		
		//Arrastra la barrita de arrastre
		public function mouseWheel(e:MouseEvent):void
		{
			if(direccion == "ver")
			{
				var mc:Object = this.getChildByName("Barrita_mc_"+direccion);
				trace("lo detecto "+direccion+" "+(alto-mc.height))				
				mc.startDrag(false,new Rectangle(0,0,0,alto-mc.height));				
			}
		}
		
		//Arrastra la barrita de arrastre
		private function Arrastrar(e:MouseEvent):void
		{						
			var mc:Sprite = e.target as Sprite;
			if(direccion == "ver")			
				mc.startDrag(false,new Rectangle(0,0,0,alto-mc.height));
			else	
				mc.startDrag(false,new Rectangle(0,0,ancho-mc.width,0));
		}
		//Suelta la barrita de arrastre
		private function Soltar(e:MouseEvent):void{
			e.target.stopDrag();
		}		
		//Función que mueve el ct_principal cuando hacemos scroll
		private function Colocar(e:Event):void
		{			
			var fin = 0;
			var porcentaje = 0;
			if(direccion == "ver")
			{				
				porcentaje = 1 - (Math.round(recorrido - (getChildByName("Barrita_mc_"+direccion).y)))/recorrido;
				fin = -((ct_principal.height - alto) * porcentaje - (y-addPos));
				ct_principal.y += (fin - ct_principal.y)/10;
				ct_segundario.y += (fin - ct_segundario.y)/10;
			}
			else
			{
				porcentaje = 1 - (Math.round(recorrido - (getChildByName("Barrita_mc_"+direccion).x)))/recorrido;
				fin = -((ct_principal.width - alto) * porcentaje - (x-addPos));
				ct_principal.x += (fin - ct_principal.x)/10;
				ct_segundario.x += (fin - ct_segundario.x)/10;
			}
		}
	}
}