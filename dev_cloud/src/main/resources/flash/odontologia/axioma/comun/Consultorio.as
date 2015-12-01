package axioma.comun{
	
	import axioma.estructuras.Lista;
	
	public class Consultorio
	{
		private var espacios:Lista;
		private var indice:int;
		private var codigo:int;
		
		public function Consultorio()
		{
			espacios = new Lista();
		}
		
		public function addEspacio(espacio:EspacioTiempo):void
		{
			espacios.add(espacio);
		}
		
		public function getEspacios():Lista
		{
			return espacios;
		}

		public function setEspacios(espacios:Lista):void
		{
			this.espacios=espacios;
		}
		
		public function setIndice(indice:int):void
		{
			this.indice=indice;
		}
		public function getIndice():int
		{
			return indice;
		}

		public function setCodigo(codigo:int):void
		{
			this.codigo=codigo;
		}
		public function getCodigo():int
		{
			return codigo;
		}

	}
	
}