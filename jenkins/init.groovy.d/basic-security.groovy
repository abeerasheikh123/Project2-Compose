import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('student','StudentPass123!')
hudsonRealm.createAccount('pipelineuser','PipelinePass123!')
instance.setSecurityRealm(hudsonRealm)

def strategy = new GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER, 'student')
strategy.add(Jenkins.READ, 'pipelineuser')
strategy.add(hudson.model.Item.READ, 'pipelineuser')
strategy.add(hudson.model.Item.BUILD, 'pipelineuser')
instance.setAuthorizationStrategy(strategy)

instance.save()
