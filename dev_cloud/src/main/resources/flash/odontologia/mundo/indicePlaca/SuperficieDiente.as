package mundo.indicePlaca
{
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.geom.ColorTransform;
	import fl.controls.CheckBox;
	import util.general.Constantes;
	
	public class SuperficieDiente extends MovieClip
	{
		//Indica el sector del diente
		var sector:String;
		
		//Indica si fue seleccionado
		var marcado:String;
		
		var codigo:String;
		
		var nombre:String;
		
		public function SuperficieDiente(param_sector:String,param_marcado:String):void
		{
			this.sector = param_sector;
			this.marcado = param_marcado;
			this.codigo = "";
			this.nombre = "";
			this.useHandCursor = true;
			this.addEventListener(MouseEvent.CLICK,pintar);
		}
		
		/**
		Rellena la superficie con el color indicado y la marca
		como seleccionado
		*/
		public function pintar(e:MouseEvent):void
		{
			var parentObjD:Object = this.parent as Object;	
			var placaObjD:Object = this.parent.parent.parent as Object;	
			
			trace(parentObjD.getAusente.selected+" "+parentObjD.getActivo+" "+this.marcado.toString())
			if(parentObjD.getAusente.selected == false && 
			   	parentObjD.getActivo == Constantes.acronimoSi)
			{				
				var testClipTransform:ColorTransform;                                                                                   
				var parentObjP:Object = this.parent.parent as Object;	
				
				if(this.marcado.toString() == Constantes.acronimoSi)
				{
					this.marcado = Constantes.acronimoNo;
					testClipTransform=new ColorTransform(0,0,0,1,255,255,255,0);
					this.transform.colorTransform = testClipTransform;
					
					placaObjD.operNumSuperDentSelec(1,"rest");
				}
				else
				{
					//incluye el diente
					parentObjD.incluirDiente();
										
					//captura el diente que es excluyente
					if(parentObjD.getDienteExcluido > 0)
					{
						var numDienteExclu:int = placaObjD.getPosNumeroDiente(parentObjD.getDienteExcluido);
						if(numDienteExclu > 0)
						{								
							parentObjP.getChildAt(numDienteExclu).excluirDiente();
						}
					}
					
					this.marcado = Constantes.acronimoSi;
					testClipTransform=new ColorTransform(0,0,0,1,284,86,86,0);
					this.transform.colorTransform = testClipTransform;
					
					placaObjD.operNumSuperDentSelec(1,"sum");
				}
			}
		}
		
		/**
		Pinta la superficie de blanco
		*/
		public function pintarBlanco():void
		{
			var testClipTransform:ColorTransform;
			this.marcado = Constantes.acronimoNo;
			testClipTransform=new ColorTransform(0,0,0,1,255,255,255,0);
			this.transform.colorTransform = testClipTransform;
		}
				
		public function get getSector():String
		{
			return this.sector;
		}
		
		public function set setSector(param_value:String):void
		{
			this.sector = param_value;
		}
		
		public function get getMarcado():String
		{
			return this.marcado;
		}
		
		public function set setMarcado(param_value:String):void
		{
			this.marcado = param_value;
		}
		
		public function get getCodigo():String
		{
			return this.codigo;
		}
		
		public function set setCodigo(param_value:String):void
		{
			this.codigo = param_value;
		}
		
		public function get getNombre():String
		{
			return this.nombre;
		}
		
		public function set setNombre(param_value:String):void
		{
			this.nombre = param_value;
		}
	}
}