local keys = redis.call('keys',KEYS[1])
local temp = {}
for iter, value in ipairs(keys) do
	table.insert(temp, { value, redis.call('del', value) })
end
return temp