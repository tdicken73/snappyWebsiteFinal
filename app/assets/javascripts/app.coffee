utils = window.angular.module('utils' , [])

utils.filter('timer' ,['time', (time) ->
  (input) ->
    if input
      seconds=toSeconds(input)
      minutes=toMinutes(input)
      hours=toHours(input)
      "#{hours}:#{minutes}:#{seconds}"
    else
      "Press Start"
]).service('time', ->
    @toHours = (timeMillis) -> addZero((timeMillis/(1000*60*60)))
    @toMinutes = (timeMillis) -> addZero((timeMillis/(1000*60))%60)
    @toSeconds = (timeMillis) -> addZero((timeMillis/1000)%60)
    @toTime = (hours,minutes,seconds) -> ((hours * 60 * 60) + (minutes * 60) + seconds) * 1000
    @addZero = (value) ->
      value = Math.floor(value)
      if(value < 10)
        "0#{value}"
      else
        value

).controller('TimerController', ($scope, $http) ->

  startWS = ->
    wsUrl = jsRoutes.controllers.AppController.indexWS().webSocketURL()
  
  
   
    $scope.socket = new WebSocket(wsUrl)
    listmaster = []
    $scope.socket.onmessage = (msg) ->
      $scope.$apply( ->
        console.log "received : #{msg.data}"
        $scope.time = JSON.parse(msg.data).data   
        
        $scope.node = JSON.parse(msg.data).node
        listappend = []
        listappend.push $scope.node
        list = $scope.node

        console.log "received  node: #{list}"
        $scope.data = JSON.parse(msg.data).data
        listdata = $scope.data
        listappend.push $scope.data
        listmaster.concat listappend
        console.log "received scope.data: #{listdata}"
        console.log "received listappend: #{listappend}"
        $scope.socket.send($scope.node)
        listmaster.concat listappend
        console.log "received listmaster: #{listappend}"
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