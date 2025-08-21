define([
  'knockout',
  'ojs/ojarraydataprovider',
  'ojs/ojbutton',
  'ojs/ojinputtext',
  'ojs/ojdatetimepicker',
  'ojs/ojselectsingle',
  'ojs/ojtable'
], function (ko, ArrayDataProvider) {

  function KycDocumentsViewModel() {
    var self = this;

    // ----------- Form Observables -----------
    self.cifNumber = ko.observable('');
    self.documentType = ko.observable();
    self.documentNumber = ko.observable('');
    //self.verificationStatus = ko.observable();

    // ----------- Dropdown Data -----------
    self.documentTypeOptions = [
      { value: 'Aadhaar', label: 'Aadhaar' },
      { value: 'PAN', label: 'PAN' },
      { value: 'Passport', label: 'Passport' },
      { value: 'Driving License', label: 'Driving License' }
    ];
    self.documentTypeDataProvider = new ArrayDataProvider(self.documentTypeOptions, { keyAttributes: 'value' });

    // self.verificationStatusOptions = [
    //   { value: 'Pending', label: 'Pending' },
    //   { value: 'Verified', label: 'Verified' },
    //   { value: 'Rejected', label: 'Rejected' }
    // ];
    // self.verificationStatusDataProvider = new ArrayDataProvider(self.verificationStatusOptions, { keyAttributes: 'value' });

    // ----------- Table Data -----------
    self.kycDocuments = ko.observableArray([]);
    self.kycDataProvider = new ArrayDataProvider(self.kycDocuments, { keyAttributes: 'kycId' });

    // ----------- Add Document Function -----------
    self.addKycDocument = function () {
      if (!self.cifNumber() || !self.documentType() || !self.documentNumber()) {
        alert("Please fill all required fields!");
        return;
      }

      const newDoc = {
        //cifNumber : self.cifNumber(),
        documentType: self.documentType(),
        documentNumber: self.documentNumber()
        //verificationStatus: self.verificationStatus()
      };

      // POST to backend using CIF number in URL
      fetch("http://localhost:9090/customers/cif/" + self.cifNumber() + "/kyc", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newDoc)
      })
      .then(res => res.json())
      .then(savedDoc => {
        self.kycDocuments.push(savedDoc); // push response with generated kycId
        // reset form
        self.cifNumber('');
        self.documentType(null);
        self.documentNumber('');
        //self.verificationStatus(null);
      })
      .catch(err => console.error("Error saving KYC:", err));
    };

    // ----------- Load Existing Docs (optional) -----------
    self.loadKycDocuments = function () {
      fetch("http://localhost:9090/customers/cif/" + self.cifNumber() + "/kyc")
        .then(res => res.json())
        .then(data => self.kycDocuments(data))
        .catch(err => console.error("Error loading KYC docs:", err));
    };
  }

  return KycDocumentsViewModel;
});
