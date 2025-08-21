
define([
    'knockout',
    'ojs/ojtable',
    'ojs/ojmodel',
    'models/CustomerCollection',
    'ojs/ojcollectiondataprovider',
    'ojs/ojbutton',
    'ojs/ojformlayout',
    'ojs/ojinputtext',
    'ojs/ojdialog'
], function (ko, ojtable, ojmodel, CustomerCollection, CollectionDataProvider) {

    function CustomersViewModel() {
        var self = this;
        self.collection = new CustomerCollection();
        self.dataProvider = ko.observable();

        self.collection.fetch({
            success: function () {

                self.dataProvider(new CollectionDataProvider(self.collection));
                console.log("Data loaded");
                self.collection.each(function (model) {
                    console.log(model.toJSON());
                });
            },
            error: function (jqXHR, textStatus) {
                console.error("Fetch error:", textStatus);
            }
        });


        self.customerId = ko.observable();
        self.userId = ko.observable();
        self.cifNumber = ko.observable();
        self.firstName = ko.observable();
        self.lastName = ko.observable();
        self.dateOfBirth = ko.observable();
        self.gender = ko.observable();
        self.phoneNumber = ko.observable();

        self.nationality = ko.observable();
        self.customerType = ko.observable();
        self.email = ko.observable();
        self.address = ko.observable();
        self.city = ko.observable();
        self.state = ko.observable();
        self.postalCode = ko.observable();
        self.country = ko.observable();


        self.openAddDialog = function () {
            document.getElementById("addDialog").open();
        };

        self.closeAddDialog = function () {
            document.getElementById("addDialog").close();
        };

        // Add new customer
        self.addCustomer = function () {
            var newCustomer = {
                firstName: self.firstName(),
                lastName: self.lastName(),
                dateOfBirth: self.dateOfBirth(),
                gender: self.gender(),
                phoneNumber: self.phoneNumber(),
                nationality: self.nationality(),
                customerType: self.customerType(),
                email: self.email(),
                address: self.address(),
                city: self.city(),
                state: self.state(),
                postalCode: self.postalCode(),
                country: self.country()
            };

            self.collection.create(newCustomer, {
                wait: true,
                success: function (model) {
                    alert(self.firstName() + " registered successfully. CIF: " + model.get('cifNumber'));
                    self.dataProvider(new CollectionDataProvider(self.collection));
                    self.closeAddDialog();
                    self.clearCustomerForm();
                },
                error: function (model, xhr) {
                    alert("Error registering customer: " + (xhr.statusText || "Unknown error"));
                }
            });
        };


        self.openEditDialog = function (row) {
            self.customerId(row.customerId);
            self.userId(row.userId);
            self.cifNumber(row.cifNumber);
            self.firstName(row.firstName);
            self.lastName(row.lastName);
            self.dateOfBirth(row.dateOfBirth);
            self.gender(row.gender);
            self.phoneNumber(row.phoneNumber);
            self.nationality(row.nationality);
            self.customerType(row.customerType);
            self.email(row.email);
            self.address(row.address);
            self.city(row.city);
            self.state(row.state);
            self.postalCode(row.postalCode);
            self.country(row.country);
            document.getElementById('editDialog').open();
        };

        self.closeEditDialog = function () {
            document.getElementById('editDialog').close();
        };

        self.updateCustomer = function () {
            var model = self.collection.get(self.customerId());
            if (model) {
                model.save({
                    userId: self.userId(),
                    cifNumber: self.cifNumber(),
                    firstName: self.firstName(),
                    lastName: self.lastName(),
                    dateOfBirth: self.dateOfBirth(),
                    gender: self.gender(),
                    phoneNumber: self.phoneNumber(),
                    nationality: self.nationality(),
                    customerType: self.customerType(),
                    email: self.email(),
                    address: self.address(),
                    city: self.city(),
                    state: self.state(),
                    postalCode: self.postalCode(),
                    country: self.country()
                }, {
                    wait: true,
                    success: function () {
                        self.dataProvider(new CollectionDataProvider(self.collection));
                        self.closeEditDialog();
                        alert("Customer updated successfully!");
                    },
                    error: function (model, xhr) {
                        alert("Error updating customer: " + (xhr.statusText || "Unknown error"));
                    }
                });
            }
        };

        // Delete visitor
        self.deleteCustomer = function (row) {
            var model = self.collection.get(row.customerId);
            if (model) {
                if (confirm("Are you sure you want to delete this customer?")) {
                    model.destroy({
                        wait: true,
                        success: function () {
                            self.dataProvider(new CollectionDataProvider(self.collection));
                            alert("Customer deleted.");
                        },
                        error: function (model, xhr) {
                            alert("Error deleting customer: " + xhr.statusText);
                        }
                    });
                }
            }
        };
    }
    return new CustomersViewModel();
});


