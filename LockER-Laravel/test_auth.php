<?php
require __DIR__.'/vendor/autoload.php';
$app = require_once __DIR__.'/bootstrap/app.php';
$app->make(Illuminate\Contracts\Console\Kernel::class)->bootstrap();

$request = new Illuminate\Http\Request();
$request->merge([
    'name' => 'Test User',
    'email' => 'test_error_script@example.com',
    'password' => 'password123',
    'role' => 'job_seeker'
]);

$controller = new App\Http\Controllers\AuthController();
$response = $controller->register($request);
echo $response->getContent();
