node {
    def mvnHome
    stage('Git Preparation') { // for display purposes
        git 'ssh://eyicwux@gerrit.ericsson.se:29418/OSS/com.ericsson.graduates/project-b1'
    }
    stage('Mvn Build') {
        // Run the maven build
        mvnHome = tool 'mvn3'
        withEnv(["MVN_HOME=$mvnHome"]) {
            if (isUnix()) {
                sh '"$MVN_HOME/bin/mvn" clean package -DskipTests=True'
            } else {
                bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
            }
        }
    }
    stage('Docker push') {
        sh '''docker build -t alber09/project-b1_web .
        
        docker login -u alber09 -p wyc264550
        
        docker image push alber09/project-b1_web'''
    }
    
    stage('K8s Deploy') {
        sh '''set -e
        echo ok
        echo ${WORKSPACE}
        scp ${WORKSPACE}/*.yaml ubuntu@100.121.115.153:/home/ubuntu/jenkinsyml
        ssh ubuntu@100.121.115.153 "kubectl apply -f /home/ubuntu/jenkinsyml/mymongo-deployment.yaml"
        ssh ubuntu@100.121.115.153 "kubectl apply -f /home/ubuntu/jenkinsyml/mymongo-service.yaml"
        ssh ubuntu@100.121.115.153 "kubectl apply -f /home/ubuntu/jenkinsyml/web-deployment.yaml"
        ssh ubuntu@100.121.115.153 "kubectl apply -f /home/ubuntu/jenkinsyml/web-service.yaml"'''
    }
}