package com.kubukoz.repro.db

import kamon.Kamon
import slick.util.AsyncExecutor

import scala.concurrent.ExecutionContext

//see https://gist.github.com/kubukoz/3e301c7a8a463ff8bdb3f89f40cef717 for updates
class AsyncExecutorContextAware(underlying: AsyncExecutor)
    extends AsyncExecutor {
  override def executionContext: ExecutionContext =
    new ExecutionContextAware(underlying.executionContext)
  override def close(): Unit = underlying.close()
}

class ExecutionContextAware(underlying: ExecutionContext)
    extends ExecutionContext {
  override def execute(runnable: Runnable): Unit =
    underlying.execute(new RunnableContextAware(runnable))
  override def reportFailure(cause: Throwable): Unit =
    underlying.reportFailure(cause)
}

class RunnableContextAware(underlying: Runnable) extends Runnable {
  private val traceContext = Kamon.currentContext

  override def run(): Unit = {
    Kamon.withContext(traceContext) {
      underlying.run()
    }
  }
}
