export function isAddressValid(address) {
    var ipv4Regex = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    if (ipv4Regex.test(address)) {
        return true;
    }

    var ipv6Regex = /^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$/;
    if (ipv6Regex.test(address)) {
        return true;
    }

    var domainRegex = /^[a-zA-Z0-9]+([\-\.]{1}[a-zA-Z0-9]+)*\.[a-zA-Z]{2,}$/;
    return domainRegex.test(address);
}
export function isPortValid(port) {
    if(port.length == 0){
        return true;
    }

    port = parseInt(port, 10);

    if (!Number.isInteger(port)) {
        return false;
    }

    if (port < 0 || port > 65535) {
        return false;
    }

    return true;
}

export function convertDate(inputDate) {
    if(inputDate == null){
        return '';
    }

    var date = new Date(inputDate);

    var day = String(date.getDate()).padStart(2, '0');
    var month = String(date.getMonth() + 1).padStart(2, '0'); 
    var year = date.getFullYear();
    
    var hours = String(date.getHours()).padStart(2, '0');
    var minutes = String(date.getMinutes()).padStart(2, '0');
    var seconds = String(date.getSeconds()).padStart(2, '0');

    var formattedDate = `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
    
    return formattedDate;
}