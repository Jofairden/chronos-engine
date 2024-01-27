package chronos.engine.core.interfaces.apis

interface IExternalApiRequest {
  val api: IExternalApi
  val endpoint: String
}
