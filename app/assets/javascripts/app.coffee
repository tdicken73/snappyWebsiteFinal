utils = window.angular.module('utils' , [])
node1 = []
node2 = []
node3 = []
node4 = []
node5 = []
node6 = []
node7 = []
node8 = []
node9 = []
node10 = []

utils.controller('TimerController', ($scope, $http) ->
  
  startWS = ->
    wsUrl = jsRoutes.controllers.AppController.indexWS().webSocketURL()
    $scope.socket = new WebSocket(wsUrl)

    $scope.socket.onmessage = (msg) ->
      $scope.$apply( ->
  
 
        $scope.node = JSON.parse(msg.data).node
        $scope.data = JSON.parse(msg.data).data
        
        #console.log "received  node: #{$scope.node}"
        #console.log "received  data: #{$scope.data}"
        
        if($scope.node == "node1")
        	node1.push $scope.data
        else if($scope.node == "node2")
        	node2.push $scope.data
        else if($scope.node == "node3")
        	node3.push $scope.data
        else if($scope.node == "node4")
        	node4.push $scope.data
        else if($scope.node == "node5")
        	node5.push $scope.data
        else if($scope.node == "node6")
        	node6.push $scope.data
        else if($scope.node == "node7")
        	node7.push $scope.data
        else if($scope.node == "node8")
        	node8.push $scope.data
        else if($scope.node == "node9")
        	node9.push $scope.data
        else if($scope.node == "node10")
        	node10.push $scope.data
        	
        if(node2.length > 20) 
        	node2 = node2.slice(-10)
        if(node3.length > 20) 
        	node3 = node3.slice(-10)
        if(node4.length > 20) 
        	node5 = node4.slice(-10)
        if(node5.length > 20) 
        	node5 = node5.slice(-10)
        if(node6.length > 20) 
        	node6 = node6.slice(-10)
        if(node7.length > 20) 
        	node7 = node7.slice(-10)
        if(node8.length > 20) 
        	node8 = node8.slice(-10)
        if(node9.length > 20) 
        	node9 = node9.slice(-10)
        if(node10.length > 20) 
        	node10 = node10.slice(-10)
        		
        $scope.node1 = node1
        $scope.node2 = node2
        $scope.node3 = node3
        $scope.node4 = node4
        $scope.node5 = node5
        $scope.node6 = node6
        $scope.node7 = node7
        $scope.node8 = node8
        $scope.node9 = node9
        $scope.node10 = node10
        
      )
	

   
  $scope.start = ->
    $http.get(jsRoutes.controllers.AppController.start().url).success( -> )

  $scope.stop = ->
    $http.get(jsRoutes.controllers.AppController.stop().url).success( -> )

  startWS()

) 

window.angular.module('app' , ['utils'])