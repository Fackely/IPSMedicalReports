package mundo.agendaOdontologica {
	
	public class ParametrosAgenda 
	{		
		// Constants:
		// Public Properties:
		// Private Properties:
		private var intervalo:int = 0;
		private var horaInicio:String = "";
		private var minutoInicio:String = "";
		private var horaFinal:String = "";
		private var minutoFinal:String = "";
		private var colorIntervalo:String = "";
		private var colorConsultorio:String = "";
	
		// Initialization:
		public function ParametrosAgenda() { 
			
		}
			
		// Public Methods:
		public function set setIntervalo(valor:int)
		{
			this.intervalo = valor;
		}
		
		public function get getIntervalo():int
		{
			return this.intervalo;
		}
		
		public function set setColorIntervalo(valor:String)
		{
			this.colorIntervalo = valor;
		}
		
		public function get getColorIntervalo():String
		{
			return this.colorIntervalo;
		}
		
		public function set setColorConsultorio(valor:String)
		{
			this.colorConsultorio = valor;
		}
		
		public function get getColorConsultorio():String
		{
			return this.colorConsultorio;
		}
		
		public function set setHoraInicio(valor:String)
		{
			this.horaInicio = valor;
		}
		
		public function get getHoraInicio():String
		{
			return this.horaInicio;
		}
		
		public function set setMinutoInicio(valor:String)
		{
			this.minutoInicio = valor;
		}
		
		public function get getMinutoInicio():String
		{
			return this.minutoInicio;
		}
		
		public function set setHoraFinal(valor:String)
		{
			this.horaFinal = valor;
		}
		
		public function get getHoraFinal():String
		{
			return this.horaFinal;
		}
		
		public function set setMinutoFinal(valor:String)
		{
			this.minutoFinal = valor;
		}
		
		public function get getMinutoFinal():String
		{
			return this.minutoFinal;
		}
		
		// Protected Methods:
	}
	
}