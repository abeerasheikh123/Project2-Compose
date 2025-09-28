#!groovy
// security.groovy
// Creates an admin user and a non-admin pipeline user and sets Matrix permissions.
// WARNING: This writes passwords in cleartext inside the image. Change passwords immediately
// and remove this script from the image/repo after initial setup.

import jenkins.model.*
import hudson.security.*
import jenkins.security.s2m.AdminWhitelistRule

def instance = Jenkins.getInstance()

try {
  // 1) Setup internal user database if not present
  if (!(instance.getSecurityRealm() instanceof HudsonPrivateSecurityRealm)) {
    def realm = new HudsonPrivateSecurityRealm(false)
    realm.createAccount("admin","AdminPass123!")       // CHANGE THIS PASSWORD AFTER FIRST LOGIN
    realm.createAccount("pipelineuser","PipelinePass123!") // non-admin pipeline user (CHANGE)
    instance.setSecurityRealm(realm)
    println "--> Created admin and pipelineuser in Jenkins internal user database."
  } else {
    println "--> HudsonPrivateSecurityRealm already present."
    def realm = (HudsonPrivateSecurityRealm) instance.getSecurityRealm()
    if (realm.getUser("pipelineuser") == null) {
      realm.createAccount("pipelineuser","PipelinePass123!")
      println "--> Created pipelineuser account."
    }
  }

  // 2) Configure Matrix-based security and disable anonymous
  def strategy = new GlobalMatrixAuthorizationStrategy()

  // Give full rights to admin
  strategy.add(Jenkins.ADMINISTER, "admin")
  strategy.add(Jenkins.READ, "admin")

  // Minimal rights for pipelineuser (adjust as needed)
  strategy.add(Item.READ, "pipelineuser")
  strategy.add(Item.BUILD, "pipelineuser")
  strategy.add(Item.CONFIGURE, "pipelineuser") // optional: remove if you want less rights
  strategy.add(View.READ, "pipelineuser")

  // Remove all anonymous rights (disable anonymous)
  // (No explicit anonymous grants added)

  instance.setAuthorizationStrategy(strategy)

  // Optionally disable CLI remoting for security (uncomment if desired)
  // instance.getDescriptor("jenkins.CLI").setEnabled(false)

  // save configuration
  instance.save()
  println "--> Security initialization completed. Please change passwords & remove this script ASAP."

} catch (Exception e) {
  println "Init script error: ${e}"
}
