LoginApp.RegisterController = Ember.Controller.extend({
  
  userName: '',
  password: '',
  confirmPassword: '',
  email: '',
  successMsg: '',
  errorMsg: '',
  inValidInputs: function() {
    return !(this.get('userName') && this.get('password') && this.get('confirmPassword') && this.get('email')
                && (this.get('password') === this.get('confirmPassword')));
  }.property('userName', 'password', 'confirmPassword', 'email'),

  disableReset: function() {
    return !(this.get('userName') || this.get('password') || this.get('confirmPassword') || this.get('email'));
  }.property('userName', 'password', 'confirmPassword', 'email'),

  passwordMatch: function() {

    return (this.get('password') && this.get('confirmPassword') &&this.get('password') !== this.get('confirmPassword')) 
          ? 'Passwords do not match' : '';

  }.property('password', 'confirmPassword'),

  validName: true,

  isChecked: false,

  actions: {
    checkName: function() {
      
      var userName = this.get('userName');
      var ctrl = this;
    
      Ember.$.get('/RESTfulExample/api/users/check?name=' + userName, function(data){
        ctrl.set('validName', !data.found);
        ctrl.set('isChecked', true);
      });
    },
    isTyping: function() {
      this.set('isChecked', false);
    },
    submit: function() {
      var ctrl = this,
        inValidInputs = this.get("inValidInputs");
      
      !inValidInputs && Ember.$.ajax({
                type        :   'POST'  ,
                url         :   '/RESTfulExample/api/users/register',
                data        :   JSON.stringify({
                                  userName: this.get('userName'),
                                  password: this.get('password'),
                                  email: this.get('email')
                                }),
                contentType :   'application/json',
                dataType    :   'json',
                success     :   function(resp) {
                                    if(resp.error) {
                                      ctrl.set('successMsg', '');
                                      ctrl.set('errorMsg', resp.message);    
                                    } else {
                                      ctrl.set('successMsg', 'Registration Successful.. Login to continue');  
                                      ctrl.set('errorMsg', '');
                                    }                               
                                    
                                },
                error       : function() {
                                ctrl.set('errorMsg', 'Error while registering');
                              }
        });        
    },
    reset: function() {
      this.set('userName', '');
      this.set('password', '');
      this.set('email', '');
      this.set('confirmPassword', '');
      this.set('successMsg', '');
      this.set('errorMsg', '');
    }
  }
});