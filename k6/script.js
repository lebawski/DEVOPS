import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10, // Number of virtual users
    duration: '30s', // Duration of the test
};

export default function () {
    const res = http.get('https://your-api-or-app-url.com/');

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1); // Pause for 1 second between iterations
}
