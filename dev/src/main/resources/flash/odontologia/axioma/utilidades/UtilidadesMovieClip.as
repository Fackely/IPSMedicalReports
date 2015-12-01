package axioma.utilidades {
	
	import flash.display.Sprite;
    import flash.text.TextFormat;
    import flash.text.TextField;
	import flash.display.CapsStyle;
	import flash.display.LineScaleMode;
	import flash.display.JointStyle;
	import flash.text.AntiAliasType;
    import flash.text.TextFieldAutoSize;

	
	public class UtilidadesMovieClip {
		
		public function UtilidadesMovieClip() { }
	
		public static function crearCelda(x:int, y:int, ancho:int, alto:int, borde:int, color:String, texto:String, formatAgenda:TextFormat, xLabel:int, yLabel:int):Sprite
		{
			var celda:Sprite = new Sprite();
			UtilidadesMovieClip.asignarPropiedadesSprite(celda, x, y, ancho, alto, borde, color, texto, formatAgenda, xLabel, yLabel);
			return celda;
		}

		public static function asignarPropiedadesSprite(celda:Sprite, x:int, y:int, ancho:int, alto:int, borde:int, color:String, texto:String, formatAgenda:TextFormat, xLabel:int, yLabel:int):void
		{
			celda.x=x;
			celda.y=y;
			celda.graphics.beginFill(uint(color));
			celda.graphics.lineStyle(1,uint(color),0.1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			celda.graphics.drawRoundRect(0, 0, ancho, alto, borde, borde);
			celda.graphics.endFill();
			celda.alpha= 0.9;
			
			// se adiciona el texto de la celda
			UtilidadesMovieClip.adicionarLabelEspacioTiempo(celda, texto, formatAgenda, xLabel, yLabel);
			
		}

		public static function adicionarLabelEspacioTiempo(celda:Sprite, texto:String, formatAgenda:TextFormat, xLabel:int, yLabel:int):void
		{
			UtilidadesMovieClip.adicionarLabelEspacioTiempoCoordenadas(celda, texto, formatAgenda, xLabel, yLabel);
		}
		
		public static function adicionarLabelEspacioTiempoCoordenadas(celda:Sprite, texto:String, formatAgenda:TextFormat, left:int, top:int):void
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


	}
	
}