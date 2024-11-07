package chronos.engine.core.api

interface IExternalApiRequest {
  val api: IExternalApi
  val endpoint: String
}
