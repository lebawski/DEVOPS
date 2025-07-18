import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10, // Number of virtual users
    duration: '30s', // Duration of the test
};

export default function () {
    // Replace with your actual endpoint path
    const res = http.get('http://localhost:8086/Foyer/etudiant');

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response time < 500ms': (r) => r.timings.duration < 500,
    });

    sleep(1); // Pause for 1 second between requests
}
