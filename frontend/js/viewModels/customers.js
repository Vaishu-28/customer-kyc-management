define([
  'knockout',
  'ojs/ojarraydataprovider',
  'ojs/ojtable',
  'ojs/ojbutton',
  'ojs/ojinputtext',
  'ojs/ojselectsingle',
  'ojs/ojdatetimepicker',
  'ojs/ojvalidationgroup',
  'ojs/ojasyncvalidator-length',
  'ojs/ojvalidator-regexp'
], function (
  ko,
  ArrayDataProvider,
  ojTable,
  ojButton,
  ojInputText,
  ojSelectSingle,
  ojDateTimePicker,
  ValidationGroup,
  AsyncLengthValidator,
  RegExpValidator
) {

  function CustomersViewModel() {
    let self = this;

    self.customers = ko.observableArray([]);
    self.customerDataProvider = new ArrayDataProvider(self.customers, { keyAttributes: "customerId" });

    self.firstName = ko.observable("");
    self.lastName = ko.observable("");
    self.dateOfBirth = ko.observable("");
    self.gender = ko.observable("");
    self.phoneNumber = ko.observable("");
    self.email = ko.observable("");
    self.nationality = ko.observable("");
    self.customerType = ko.observable("");
    self.address = ko.observable("");
    self.city = ko.observable("");
    self.state = ko.observable("");
    self.postalCode = ko.observable("");
    self.country = ko.observable("");

    
    self.firstNameValidators = ko.observableArray([
      new AsyncLengthValidator({ min: 2, max: 20 })
    ]);
    self.lastNameValidators = ko.observableArray([
      new AsyncLengthValidator({ min: 2, max: 20 })
    ]);

    self.phoneValidator = new RegExpValidator({
      pattern: "^[0-9]{10}$",
      hint: "Enter exactly 10 digits",
      messageDetail: "Invalid phone number"
    });
    self.phoneValidators = ko.observableArray([self.phoneValidator]);

    self.emailValidator = new RegExpValidator({
      pattern: "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      hint: "Enter a valid email",
      messageDetail: "Invalid email format"
    });
    self.emailValidators = ko.observableArray([self.emailValidator]);


    self.genderOptions = [
      { value: 'Male', label: 'Male' },
      { value: 'Female', label: 'Female' },
      { value: 'Other', label: 'Other' }
    ];
    self.genderDataProvider = new ArrayDataProvider(self.genderOptions, { keyAttributes: 'value' });

    self.isFormInvalid = ko.observable(true);
    self.groupValidChanged = (event) => {
      self.isFormInvalid(event.detail.value !== 'valid');
    };


    self.addCustomer = async function () {
      let form = document.getElementById('customerForm');
      if (form.valid !== 'valid') {
        form.showMessages();
        form.focusOn('@firstInvalidShown');
        return;
      }

      const newCustomer = {
        customerId: "CUST00" + (self.customers().length + 1),  
        userId: self.customers().length + 1,              
        firstName: self.firstName(),
        lastName: self.lastName(),
        dateOfBirth: self.dateOfBirth(),                   
        gender: self.gender(),
        phoneNumber: self.phoneNumber(),
        email: self.email(),
        nationality: self.nationality(),
        customerType: self.customerType(),
        address: self.address(),
        city: self.city(),
        state: self.state(),
        postalCode: self.postalCode(),
        country: self.country()
      };

      // Add to local array
      self.customers.push(newCustomer);

      // Optional: send to backend
      try {
        await fetch("http://localhost:9092/customers", {
          method: 'POST',
          headers: { "Content-Type": "application/json; charset=UTF-8" },
          body: JSON.stringify(newCustomer)
        });
      } catch (err) {
        console.error("Network error:", err);
      }


      // Reset form
      self.firstName(""); self.lastName(""); self.dateOfBirth(""); self.gender("");
      self.phoneNumber(""); self.email(""); self.nationality(""); self.customerType("");
      self.address(""); self.city(""); self.state(""); self.postalCode(""); self.country("");
    };
  }

  return CustomersViewModel;
});
