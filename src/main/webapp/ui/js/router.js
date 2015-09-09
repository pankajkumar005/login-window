LoginApp.Router.map(function() {
    this.route('login', { path: '/' });
    this.route('register', { path: '/register' });
    this.route('authenticated', { path: '/authenticated' }); 
});

LoginApp.LoginRoute = Ember.Route.extend({
  setupController: function(controller) {
    controller.set('title', 'Please Login to Continue');
  },
  renderTemplate: function() {
  	var controller = this.controllerFor('login');
  	controller.set('loginError', '');
  	controller.set('userName', '');
  	controller.set('password', '');
  	this.render();
  }
});

LoginApp.RegisterRoute = Ember.Route.extend({
  setupController: function(controller) {    
    controller.set('title', 'Please Provide Details Below');
  },
  renderTemplate: function() {
  	var controller = this.controllerFor('register');
  	controller.set('successMsg', '');
  	controller.set('errorMsg', '');
  	controller.set('email', '');
  	controller.set('userName', '');
  	controller.set('password', '');
  	controller.set('confirmPassword', '');
  	this.render();
  }
});

LoginApp.AuthenticatedRoute = Ember.Route.extend({
  renderTemplate: function() {
  	var controller = this.controllerFor('login'),
  		loggedIn = controller.get('loggedIn');
  	if(loggedIn) {
  		controller.set('title', 'Welcome');
  		this.render('authenticated', {
  			controller: controller
  		});
  	} else {
  		this.render('login');
  	}
  }
});