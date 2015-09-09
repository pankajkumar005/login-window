LoginApp.LoginController = Ember.Controller.extend({
  
  userName: '',
  password: '',
  loggedIn: false,
  loginError: '',
  disableLogin: function() {
    return (!this.get('userName') || !this.get('password'));
  }.property('userName', 'password'),

  actions: {
    login: function() {
      var userName = this.get('userName'),
          password = this.get('password'),
          ctrl = this;
      if(userName && password) {
        Ember.$.ajax({
                type:'POST'  ,
                url:'/RESTfulExample/api/users/validate',
                data: JSON.stringify({
                                  userName: this.get('userName'),
                                  password: this.get('password')
                                }),
                contentType: 'application/json',
                dataType: 'json',
                success: function(resp) {
                    if(resp.valid) {
                      ctrl.set('loggedIn', true);
                      ctrl.set('email', resp.email);
                      ctrl.transitionToRoute('authenticated');
                    } else if(resp.error && resp.message){
                      ctrl.set('loginError', resp.message);
                    } else {
                      ctrl.set('loginError', 'User Name and Password do not match');
                    }
                    
                }
        });  
      } else {
        //show error message
      }    
              
    },
    logout: function() {
      this.set('loggedIn', false);
      this.transitionToRoute('login');
    }
  }
});