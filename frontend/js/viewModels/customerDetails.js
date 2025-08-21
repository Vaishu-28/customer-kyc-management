define([
    'knockout',
    'ojs/ojformlayout',
    'ojs/ojinputtext',
    'ojs/ojbutton'
], function (ko) {
    function CustomerByCifViewModel() {
        var self = this;

        // Search input
        self.cifNumberToSearch = ko.observable();

        // Holds customer details after search
        self.foundCustomer = ko.observable();

        // Search function
        self.getCustomerByCif = async function () {
            const cif = self.cifNumberToSearch();
            if (!cif) {
                alert("Please enter a CIF Number.");
                return;
            }
            try {
                const response = await fetch(
                    `http://localhost:9090/customers/cif/${encodeURIComponent(cif)}`,
                    {
                        method: "GET",
                        headers: { "Accept": "application/json" }
                    }
                );

                if (response.ok) {
                    const customer = await response.json();
                    self.foundCustomer(customer);
                } else if (response.status === 404) {
                    self.foundCustomer(null);
                    alert("No customer found with this CIF Number.");
                } else {
                    self.foundCustomer(null);
                    alert("Error fetching customer: " + response.statusText);
                }
            } catch (err) {
                console.error("Network error:", err);
                self.foundCustomer(null);
                alert("Network error occurred while fetching customer.");
            }
        };
    }
    return new CustomerByCifViewModel();
});