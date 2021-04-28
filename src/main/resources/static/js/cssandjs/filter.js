let zipcode = document.forms.zipcode_form.zipcode.value;
let range = document.forms.zipcode_form.range.value;

const zipcodeFilter = () => {
	let settings = {
		"url": "https://www.zipcodeapi.com/rest/" + zipKey + "/radius.csv/" + zipcode + "/" + range + "/miles?minimal",
		"method": "GET"
	}
	$.ajax(settings).done(function (response) {
		document.getElementById("params").setAttribute("value", response);
	});
}
document.getElementById("set_filter").addEventListener("change", zipcodeFilter);