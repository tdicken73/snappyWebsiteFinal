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

addZero = (value) ->
  value = Math.floor(value)
  if(value < 10)
    "0#{value}"
  else
    value

toHours = (time) ->
  addZero((time/(1000*60*60)))
toMinutes = (time) ->
  addZero((time/(1000*60))%60)
toSeconds = (time) ->
  addZero((time/1000)%60)