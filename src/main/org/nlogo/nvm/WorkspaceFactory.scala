// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.nvm

trait WorkspaceFactory {
  def newInstance: Workspace
}
