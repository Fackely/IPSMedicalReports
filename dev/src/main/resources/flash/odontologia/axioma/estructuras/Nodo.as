package axioma.estructuras{
	
	public class Nodo{

		private var siguiente:Nodo;	
		private var anterior:Nodo;	
		private var valor:Object;

		public function Nodo(valor:Object)
		{
			this.valor = valor;
			this.siguiente=null;
			this.anterior=null;
		}

		public function getValor():Object
		{
			return valor;
		}
		public function setValor(valor:Object):void
		{
			this.valor=valor;
		}
		public function getSiguiente():Nodo
		{
			return siguiente;
		}
		public function setSiguiente(siguiente:Nodo):void
		{
			this.siguiente=siguiente;
		}
		public function getAnterior():Nodo
		{
			return anterior;
		}
		public function setAnterior(anterior:Nodo):void
		{
			this.anterior=anterior;
		}
	}
	
	
	
}